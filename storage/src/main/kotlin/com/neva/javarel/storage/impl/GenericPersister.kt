package com.neva.javarel.storage.impl

import com.neva.javarel.storage.api.Persister
import org.apache.derby.jdbc.EmbeddedDataSource
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.apache.openjpa.enhance.RuntimeUnenhancedClassesModes
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.spi.PersistenceProvider

@Service
@Component(immediate = true)
class GenericPersister : Persister {

    @Reference
    private lateinit var provider: PersistenceProvider

    override fun getEntityManagerFactory(persistenceUnitName: String): EntityManagerFactory {
        val properties = Properties();

        // TODO put data source object directly into property ...Factory


        val derbyDatasource = EmbeddedDataSource()
        derbyDatasource.setDatabaseName("target/testDB");
        derbyDatasource.setUser("test");
        derbyDatasource.setCreateDatabase("create");

        properties.put("openjpa.ConnectionFactory", derbyDatasource)

        properties.put("javax.persistence.provider", "org.apache.openjpa.persistence.PersistenceProviderImpl");
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
//        properties.put("javax.persistence.jdbc.driver", "org.apache.derby.jdbc.EmbeddedDriver")
//        properties.put("javax.persistence.jdbc.url", "jdbc:derby:target/derbydb3;create=true")
        properties.put("openjpa.jdbc.SynchronizeMappings", "buildSchema")
        properties.put("openjpa.RuntimeUnenhancedClasses",  RuntimeUnenhancedClassesModes.UNSUPPORTED)

        return provider.createEntityManagerFactory(persistenceUnitName, properties)
    }

}