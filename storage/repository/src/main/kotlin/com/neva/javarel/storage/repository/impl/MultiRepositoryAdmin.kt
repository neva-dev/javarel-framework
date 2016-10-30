package com.neva.javarel.storage.repository.impl

import com.neva.javarel.foundation.api.scanning.BundleScanner
import com.neva.javarel.foundation.api.scanning.BundleWatcher
import com.neva.javarel.foundation.api.scanning.ComponentScanBundleFilter
import com.neva.javarel.storage.repository.api.Repository
import com.neva.javarel.storage.repository.api.RepositoryAdmin
import com.neva.javarel.storage.repository.api.RepositoryConnection
import com.neva.javarel.storage.repository.api.RepositoryException
import org.apache.felix.scr.annotations.*
import org.mongodb.morphia.Morphia
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.mapping.Mapper
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.slf4j.LoggerFactory

@Component(immediate = true)
@Service(RepositoryAdmin::class, BundleWatcher::class)
class MultiRepositoryAdmin : RepositoryAdmin, BundleWatcher {

    companion object {
        val LOG = LoggerFactory.getLogger(MultiRepositoryAdmin::class.java)

        @Property(name = NAME_DEFAULT_PROP, value = MultiRepositoryConnection.NAME_DEFAULT, label = "Default connection name")
        const val NAME_DEFAULT_PROP = "nameDefault"

        val ENTITY_FILTER = ComponentScanBundleFilter(setOf(Entity::class.java))
    }

    @Reference(referenceInterface = RepositoryConnection::class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private var allConnections: MutableMap<String, RepositoryConnection> = mutableMapOf()

    private var allConnectedRepositories: MutableMap<String, Repository> = mutableMapOf()

    @Reference
    private lateinit var bundleScanner: BundleScanner

    private lateinit var bundleContext: BundleContext

    private lateinit var props: Map<String, Any>

    @Activate
    protected fun activate(bundleContext: BundleContext, props: Map<String, Any>) {
        this.bundleContext = bundleContext
        this.props = props
    }

    private val nameDefault: String
        get() = props[NAME_DEFAULT_PROP] as String

    private val mapper: Mapper by lazy {
        val mapper = Mapper()
        mapper.options.objectFactory = BundleObjectFactory(bundleContext)

        mapper
    }

    override fun repository(): Repository {
        return repository(nameDefault)
    }

    override fun repository(connectionName: String): Repository {
        var repository = allConnectedRepositories[connectionName]
        if (repository == null) {
            repository = connect(connectionByName(connectionName))
            allConnectedRepositories.put(connectionName, repository)
        }

        return repository
    }

    private fun connectionByName(name: String): RepositoryConnection {
        return allConnections[name] ?: throw RepositoryException("Repository connection named '$name' is not defined.")
    }

    private fun connect(connection: RepositoryConnection): Repository {
        val classes = bundleScanner.scan(ENTITY_FILTER)
        val morphia = Morphia(mapper, classes)
        val dataStore = morphia.createDatastore(connection.client, connection.dbName)

        return ConnectedRepository(connection, dataStore)
    }

    private fun bindAllConnections(connection: RepositoryConnection) {
        check(connection)
        allConnections.put(connection.name, connection)
    }

    private fun unbindAllConnections(connection: RepositoryConnection) {
        disconnect(connection)
        allConnections.remove(connection.name)
    }

    private fun check(connection: RepositoryConnection) {
        if (allConnections.contains(connection.name)) {
            LOG.warn("Repository connection named '${connection.name}' of type '${connection.javaClass.canonicalName}' overrides '${allConnections[connection.name]!!.javaClass}'.")
        }
    }

    private fun disconnect(connection: RepositoryConnection) {
        if (allConnectedRepositories.contains(connection.name)) {
            LOG.info("Repository connection named '${connection.name}' is being disconnected.")
            allConnectedRepositories.remove(connection.name)
        }
    }

    override val connections: Set<RepositoryConnection>
        get() = allConnections.values.toSet()

    override val connectedRepositories: Set<Repository>
        get() = allConnectedRepositories.values.toSet()

    override fun watch(event: BundleEvent) {
        if (ENTITY_FILTER.filterBundle(event.bundle)) {
            allConnectedRepositories.clear()
        }
    }
}