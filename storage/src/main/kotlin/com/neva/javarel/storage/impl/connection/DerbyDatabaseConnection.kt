package com.neva.javarel.storage.impl.connection

import com.neva.javarel.foundation.api.JavarelConstants
import org.apache.derby.jdbc.EmbeddedDataSource
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import javax.sql.DataSource

@Component(immediate = true, configurationFactory = true, metatype = true, label = "${JavarelConstants.servicePrefix} Storage - Derby Connection")
@Service
class DerbyDatabaseConnection : AbstractDatabaseConnection() {

    companion object {
        @Property(name = nameProp, value = "derby", label = "Connection name", description = "Unique identifier")
        const val nameProp = "name"

        @Property(name = userProp, value = "root", label = "Username")
        const val userProp = "userProp"

        @Property(name = dbNameProp, value = "javarel", label = "Database name")
        const val dbNameProp = "dbNameProp"
    }

    private var props: Map<String, Any>? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    private val user: String
        get() = props!![userProp] as String

    private val dbName: String
        get() = props!![dbNameProp] as String

    override val name: String
        get() = props!![nameProp] as String

    override val source: DataSource
        get() {
            val ds = EmbeddedDataSource()
            ds.setDatabaseName(dbName);
            ds.setUser(user);
            ds.setCreateDatabase("create");

            return ds
        }

}