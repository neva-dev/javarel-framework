package com.neva.javarel.storage.database.impl

import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.foundation.api.scanning.BundleScanner
import com.neva.javarel.foundation.api.scanning.BundleWatcher
import com.neva.javarel.foundation.api.scanning.ComponentScanBundleFilter
import com.neva.javarel.storage.database.api.Database
import com.neva.javarel.storage.database.api.DatabaseAdmin
import com.neva.javarel.storage.database.api.DatabaseConnection
import com.neva.javarel.storage.database.api.DatabaseException
import com.neva.javarel.storage.database.impl.connection.DerbyEmbeddedDatabaseConnection
import org.apache.felix.scr.annotations.*
import org.apache.openjpa.persistence.PersistenceProviderImpl
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.slf4j.LoggerFactory
import java.util.Properties
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.spi.PersistenceProvider
import javax.persistence.spi.PersistenceUnitTransactionType

@Component(immediate = true, metatype = true, label = "${JavarelConstants.SERVICE_PREFIX} Storage - Database Admin")
@Service(DatabaseAdmin::class, BundleWatcher::class)
class MultiDatabaseAdmin : DatabaseAdmin, BundleWatcher {

    companion object {
        val LOG = LoggerFactory.getLogger(MultiDatabaseAdmin::class.java)

        @Property(name = NAME_DEFAULT_PROP, value = DerbyEmbeddedDatabaseConnection.NAME_DEFAULT, label = "Default connection name")
        const val NAME_DEFAULT_PROP = "nameDefault"

        val ENTITY_FILTER = ComponentScanBundleFilter(setOf(Entity::class.java))

        val ENTITY_MANAGER_CONFIG = mapOf<String, Any>(
                "openjpa.DynamicEnhancementAgent" to "true",
                "openjpa.RuntimeUnenhancedClasses" to "supported",
                "openjpa.jdbc.SynchronizeMappings" to "buildSchema(foreignKeys=true')",
                "openjpa.jdbc.MappingDefaults" to "ForeignKeyDeleteAction=restrict, JoinForeignKeyDeleteAction=restrict"
        )
    }

    @Reference
    private lateinit var provider: PersistenceProvider

    @Reference
    private lateinit var bundleScanner: BundleScanner

    @Reference(referenceInterface = DatabaseConnection::class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private var allConnections: MutableMap<String, DatabaseConnection> = mutableMapOf()

    private var allConnectedDatabases: MutableMap<String, Database> = mutableMapOf()

    private lateinit var context: BundleContext

    private lateinit var props: Map<String, Any>

    @Activate
    protected fun start(context: BundleContext, props: Map<String, Any>) {
        this.context = context
        this.props = props
    }

    override val connectionDefault: String
        get() = props[NAME_DEFAULT_PROP] as String

    override fun database(): Database {
        return database(connectionDefault)
    }

    @Synchronized
    override fun database(connectionName: String): Database {
        var database = allConnectedDatabases[connectionName]
        if (database == null || !database.connected) {
            database = connect(connectionByName(connectionName))
            allConnectedDatabases.put(connectionName, database)
        }

        return database
    }

    override val connections: Set<DatabaseConnection>
        get() = allConnections.values.toSet()

    override val connectedDatabases: Set<Database>
        get() = allConnectedDatabases.values.toSet()

    private fun connectionByName(name: String): DatabaseConnection {
        return allConnections[name] ?: throw DatabaseException("Database connection named '$name' is not defined.")
    }

    private fun connect(connection: DatabaseConnection): Database {
        val props = Properties()
        props.put("openjpa.ConnectionFactory", connection.source)
        props.putAll(ENTITY_MANAGER_CONFIG)

        val info = BundlePersistenceInfo(context)
        info.persistenceProviderClassName = PersistenceProviderImpl::class.java.canonicalName
        info.persistenceUnitName = connection.name
        info.transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL
        info.setExcludeUnlistedClasses(false)

        val classes = bundleScanner.scan(ENTITY_FILTER)
        for (clazz in classes) {
            info.addManagedClassName(clazz.canonicalName)
        }

        connection.configure(info, props)

        val emf = provider.createContainerEntityManagerFactory(info, props)

        return ConnectedDatabase(connection, emf)
    }

    private fun check(connection: DatabaseConnection) {
        if (allConnections.contains(connection.name)) {
            LOG.warn("Database connection named '${connection.name}' of type '${connection.javaClass.canonicalName}' overrides '${allConnections[connection.name]!!.javaClass}'.")
        }
    }

    private fun disconnect(connection: DatabaseConnection) {
        if (allConnectedDatabases.contains(connection.name)) {
            LOG.info("Database connection named '${connection.name}' is being disconnected.")
            allConnectedDatabases.remove(connection.name)
        }
    }

    private fun bindAllConnections(connection: DatabaseConnection) {
        check(connection)
        allConnections.put(connection.name, connection)
    }

    private fun unbindAllConnections(connection: DatabaseConnection) {
        disconnect(connection)
        allConnections.remove(connection.name)
    }

    override fun <R> session(connectionName: String, callback: (EntityManager) -> R): R {
        return database(connectionName).session(callback)
    }

    override fun <R> session(callback: (EntityManager) -> R): R {
        return database().session(callback)
    }

    override fun watch(event: BundleEvent) {
        if (ENTITY_FILTER.filterBundle(event.bundle)) {
            allConnectedDatabases.clear()
        }
    }

}