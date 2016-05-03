package com.neva.javarel.storage.impl

import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.storage.api.Database
import com.neva.javarel.storage.api.DatabaseAdmin
import com.neva.javarel.storage.api.DatabaseConnection
import com.neva.javarel.storage.api.DatabaseException
import org.apache.felix.scr.annotations.*
import org.apache.openjpa.enhance.RuntimeUnenhancedClassesModes
import org.apache.openjpa.persistence.PersistenceProviderImpl
import org.osgi.framework.BundleContext
import org.slf4j.LoggerFactory
import java.util.Properties
import javax.persistence.EntityManager
import javax.persistence.spi.PersistenceProvider
import javax.persistence.spi.PersistenceUnitTransactionType

@Component(immediate = true, metatype = true, label = "${JavarelConstants.servicePrefix} Storage - Database Admin")
@Service
class GenericDatabaseAdmin : DatabaseAdmin {

    companion object {
        val log = LoggerFactory.getLogger(GenericDatabaseAdmin::class.java)

        @Property(name = nameDefaultProp, value = "derby", label = "Default connection name")
        const val nameDefaultProp = "nameDefault"
    }

    @Reference
    private lateinit var provider: PersistenceProvider

    @Reference(referenceInterface = DatabaseConnection::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC, bind = "bindConnection", unbind = "unbindConnection")
    private var _connections: MutableMap<String, DatabaseConnection> = mutableMapOf()

    private var _connectedDatabases: MutableMap<String, Database> = mutableMapOf()

    private var context: BundleContext? = null

    private var props: Map<String, Any>? = null

    @Activate
    protected fun start(context: BundleContext, props: Map<String, Any>) {
        this.context = context
        this.props = props
    }

    private val nameDefault: String
        get() = props!![nameDefaultProp] as String

    override fun database(): Database {
        return database(nameDefault)
    }

    @Synchronized
    override fun database(connectionName: String): Database {
        var database = _connectedDatabases.get(connectionName)
        if (database == null || !database.connected) {
            database = connect(connection(connectionName))
            _connectedDatabases.put(connectionName, database)
        }

        return database
    }

    override val connections: Set<DatabaseConnection>
        get() = _connections.values.toSet()

    override val connectedDatabases: Set<Database>
        get() = _connectedDatabases.values.toSet()

    private fun connection(name: String): DatabaseConnection {
        return _connections.get(name) ?: throw DatabaseException("Database connection named '$name' is not defined.")
    }

    private fun connect(connection: DatabaseConnection): Database {
        val properties = Properties();

        properties.put("openjpa.ConnectionFactory", connection.source)
        properties.put("openjpa.DynamicEnhancementAgent", "true")
        properties.put("openjpa.RuntimeUnenhancedClasses", RuntimeUnenhancedClassesModes.SUPPORTED)
        properties.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(foreignKeys=true')")
        properties.put("openjpa.jdbc.MappingDefaults", "ForeignKeyDeleteAction=restrict, JoinForeignKeyDeleteAction=restrict")

        val info = BundlePersistenceInfo(context!!)
        info.persistenceProviderClassName = PersistenceProviderImpl::class.java.canonicalName
        info.persistenceUnitName = connection.name
        info.transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL
        info.addManagedClassName("com.neva.javarel.app.adm.auth.User") // TODO scan bundles for such entries sep. with ';'
        info.setExcludeUnlistedClasses(false)

        val emf = provider.createContainerEntityManagerFactory(info, properties)

        return GenericDatabase(connection, emf)
    }

    private fun check(connection: DatabaseConnection) {
        if (_connections.contains(connection.name)) {
            log.warn("Connection named '${connection.name}' of type '${connection.javaClass.canonicalName}' overrides '${_connections[connection.name]!!.javaClass}'.")
        }
    }

    private fun disconnect(connection: DatabaseConnection) {
        if (_connectedDatabases.contains(connection.name)) {
            log.info("Connection named '${connection.name}' is being disconnected.")
            _connectedDatabases.remove(connection.name)
        }
    }

    private fun bindConnection(connection: DatabaseConnection) {
        check(connection)
        _connections.put(connection.name, connection)
    }

    private fun unbindConnection(connection: DatabaseConnection) {
        disconnect(connection)
        _connections.remove(connection.name)
    }

    override fun <R> session(callback: (EntityManager) -> R): R {
        return database().session(callback)
    }

}