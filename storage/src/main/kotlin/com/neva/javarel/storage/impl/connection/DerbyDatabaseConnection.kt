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
        @Property(name = NAME_PROP, value = "derby", label = "Connection name", description = "Unique identifier")
        const val NAME_PROP = "name"

        @Property(name = USER_PROP, value = "root", label = "Username")
        const val USER_PROP = "userProp"

        @Property(name = DB_NAME_PROP, value = "javarel", label = "Database name")
        const val DB_NAME_PROP = "dbNameProp"
    }

    private var props: Map<String, Any>? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    private val user: String
        get() = props!![USER_PROP] as String

    private val dbName: String
        get() = props!![DB_NAME_PROP] as String

    override val name: String
        get() = props!![NAME_PROP] as String

    override val source: DataSource
        get() {
            val ds = EmbeddedDataSource()
            ds.setDatabaseName(dbName);
            ds.setUser(user);
            ds.setCreateDatabase("create");

            return ds
        }

}