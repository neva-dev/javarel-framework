package com.neva.javarel.storage.impl.connection

import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.storage.api.Connection
import org.apache.derby.jdbc.EmbeddedDataSource
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import javax.sql.DataSource

@Component(immediate = true, configurationFactory = true, metatype = true, label = "${JavarelConstants.servicePrefix} Storage - Derby Connection")
@Service
class DerbyConnection : Connection {

    companion object {
        @Property(name = nameProp, value = "derby", label = "Connection name", description = "Unique identifier")
        const val nameProp = "name"
    }

    private var props: Map<String, Any>? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    override val name: String
        get() = props!![MySqlConnection.nameProp] as String

    override val source: DataSource
        get() {
            val ds = EmbeddedDataSource()
            ds.setDatabaseName("target/testDB");
            ds.setUser("test");
            ds.setCreateDatabase("create");

            return ds
        }

}