package com.neva.javarel.storage.impl

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import com.neva.javarel.storage.api.Persister
import org.apache.derby.jdbc.EmbeddedDataSource
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.apache.openjpa.enhance.RuntimeUnenhancedClassesModes
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.spi.PersistenceProvider
import javax.sql.DataSource

@Service
@Component(immediate = true)
class GenericPersister : Persister {

    @Reference
    private lateinit var provider: PersistenceProvider

    override fun getEntityManagerFactory(persistenceUnitName: String): EntityManagerFactory {
        val properties = Properties();


        properties.put("openjpa.ConnectionFactory", mysqlDataSource())

        properties.put("exclude-unlisted-classes", "false")

        properties.put("javax.persistence.provider", "org.apache.openjpa.persistence.PersistenceProviderImpl");
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");

        properties.put("openjpa.jdbc.SynchronizeMappings", "buildSchema")
        properties.put("openjpa.DynamicEnhancementAgent", "true")
        properties.put("openjpa.RuntimeUnenhancedClasses",  RuntimeUnenhancedClassesModes.SUPPORTED)

        return provider.createEntityManagerFactory(persistenceUnitName, properties)
    }

    private fun derbyDataSource(): EmbeddedDataSource {
        val derbyDatasource = EmbeddedDataSource()
        derbyDatasource.setDatabaseName("target/testDB");
        derbyDatasource.setUser("test");
        derbyDatasource.setCreateDatabase("create");
        return derbyDatasource
    }

    private fun mysqlDataSource() : DataSource {
        val dataSource = MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/javarel");
      //  dataSource.databaseName = "javarel"
        dataSource.user = "root"
        dataSource.setPassword("toor")

        return dataSource;
    }

}