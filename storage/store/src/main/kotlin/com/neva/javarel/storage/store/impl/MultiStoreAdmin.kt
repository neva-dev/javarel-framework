package com.neva.javarel.storage.store.impl

import com.neva.javarel.foundation.api.scanning.BundleScanner
import com.neva.javarel.foundation.api.scanning.BundleWatcher
import com.neva.javarel.foundation.api.scanning.ComponentScanBundleFilter
import com.neva.javarel.storage.store.api.Store
import com.neva.javarel.storage.store.api.StoreAdmin
import com.neva.javarel.storage.store.api.StoreConnection
import com.neva.javarel.storage.store.api.StoreException
import org.apache.felix.scr.annotations.*
import org.mongodb.morphia.Morphia
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.mapping.Mapper
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.slf4j.LoggerFactory

@Component(immediate = true)
@Service(StoreAdmin::class, BundleWatcher::class)
class MultiStoreAdmin : StoreAdmin, BundleWatcher {

    companion object {
        val LOG = LoggerFactory.getLogger(MultiStoreAdmin::class.java)

        @Property(name = NAME_DEFAULT_PROP, value = MultiStoreConnection.NAME_DEFAULT, label = "Default connection name")
        const val NAME_DEFAULT_PROP = "nameDefault"

        val ENTITY_FILTER = ComponentScanBundleFilter(setOf(Entity::class.java))
    }

    @Reference(referenceInterface = StoreConnection::class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private var allConnections: MutableMap<String, StoreConnection> = mutableMapOf()

    private var allConnectedStores: MutableMap<String, Store> = mutableMapOf()

    @Reference
    private lateinit var bundleScanner: BundleScanner

    private lateinit var bundleContext: BundleContext

    private lateinit var props: Map<String, Any>

    @Activate
    protected fun activate(bundleContext: BundleContext, props: Map<String, Any>) {
        this.bundleContext = bundleContext
        this.props = props
    }

    override val connectionDefault: String
        get() = props[NAME_DEFAULT_PROP] as String

    override fun store(): Store {
        return store(connectionDefault)
    }

    override fun store(connectionName: String): Store {
        var store = allConnectedStores[connectionName]
        if (store == null) {
            store = connect(connectionByName(connectionName))
            allConnectedStores.put(connectionName, store)
        }

        return store
    }

    private fun connectionByName(name: String): StoreConnection {
        return allConnections[name] ?: throw StoreException("Store connection named '$name' is not defined.")
    }

    private fun connect(connection: StoreConnection): Store {
        val classes = bundleScanner.scan(ENTITY_FILTER)
        val mapper = Mapper()
        mapper.options.objectFactory = EntityObjectFactory(classes)

        val morphia = Morphia(mapper, classes)
        val dataStore = morphia.createDatastore(connection.client, connection.dbName)

        dataStore.ensureIndexes()

        return ConnectedStore(connection, dataStore)
    }

    private fun bindAllConnections(connection: StoreConnection) {
        check(connection)
        allConnections.put(connection.name, connection)
    }

    private fun unbindAllConnections(connection: StoreConnection) {
        disconnect(connection)
        allConnections.remove(connection.name)
    }

    private fun check(connection: StoreConnection) {
        if (allConnections.contains(connection.name)) {
            LOG.warn("Store connection named '${connection.name}' of type '${connection.javaClass.canonicalName}' overrides '${allConnections[connection.name]!!.javaClass}'.")
        }
    }

    private fun disconnect(connection: StoreConnection) {
        if (allConnectedStores.contains(connection.name)) {
            LOG.info("Store connection named '${connection.name}' is being disconnected.")
            allConnectedStores.remove(connection.name)
        }
    }

    override val connections: Set<StoreConnection>
        get() = allConnections.values.toSet()

    override val connectedStores: Set<Store>
        get() = allConnectedStores.values.toSet()

    override fun watch(event: BundleEvent) {
        if (ENTITY_FILTER.filterBundle(event.bundle)) {
            allConnectedStores.clear()
        }
    }
}