package com.neva.javarel.storage.impl

import com.neva.javarel.storage.api.Connection
import com.neva.javarel.storage.api.Database
import com.neva.javarel.storage.api.DatabaseException
import com.neva.javarel.storage.api.Storage
import org.apache.felix.scr.annotations.*
import org.apache.openjpa.enhance.RuntimeUnenhancedClassesModes
import org.apache.openjpa.persistence.PersistenceProviderImpl
import org.osgi.framework.BundleContext
import java.util.Properties
import javax.persistence.spi.PersistenceProvider
import javax.persistence.spi.PersistenceUnitTransactionType

@Service
@Component(immediate = true)
class GenericStorage : Storage {

    companion object {
        val nameDefault = "derby"
    }

    private var context: BundleContext? = null

    @Reference(referenceInterface = Connection::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var connections: MutableMap<String, Connection> = mutableMapOf()

    private var connectedDatabases: MutableMap<String, Database> = mutableMapOf()

    @Activate
    protected fun start(context: BundleContext) {
        this.context = context
    }

    @Reference
    private lateinit var provider: PersistenceProvider

    override fun db(): Database {
        return db(nameDefault)
    }

    @Synchronized
    override fun db(connectionName: String): Database {
        var database = connectedDatabases.get(connectionName)
        if (database == null || !database.connected) {
            database = connect(connection(connectionName))
            connectedDatabases.put(connectionName, database)
        }

        return database
    }

    private fun connection(name: String): Connection {
        return connections.get(name) ?: throw DatabaseException("Database connection named '$name' is not defined.")
    }

    private fun connect(connection: Connection): Database {
        val properties = Properties();

        properties.put("openjpa.ConnectionFactory", connection.source)
        properties.put("openjpa.DynamicEnhancementAgent", "true")
        properties.put("openjpa.RuntimeUnenhancedClasses", RuntimeUnenhancedClassesModes.SUPPORTED)
        // properties.put("openjpa.MetaDataFactory", "jpa(Types=)")

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

    private fun bindConnections(connection: Connection) {
        connections.put(connection.name, connection)
    }

    private fun unbindConnections(connection: Connection) {
        connections.remove(connection.name)
    }

}