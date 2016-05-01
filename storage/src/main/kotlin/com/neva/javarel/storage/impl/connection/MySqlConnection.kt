package com.neva.javarel.storage.impl.connection

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.storage.api.Connection
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import javax.sql.DataSource

@Component(immediate = true, configurationFactory = true, metatype = true, label = "${JavarelConstants.servicePrefix} Storage - MySQL Connection")
@Service
class MySqlConnection : Connection {

    companion object {
        @Property(name = nameProp, value = "mysql", label = "Connection name", description = "Unique identifier")
        const val nameProp = "name"

        @Property(name = urlProp, value = "jdbc:mysql://localhost:3306/javarel", label = "JDBC url", description = "Should starts with jdbc:mysql://")
        const val urlProp = "urlProp"

        @Property(name = userProp, value = "root", label = "Username")
        const val userProp = "userProp"

        @Property(name = passwordProp, value = "toor", label = "Password")
        const val passwordProp = "passwordProp"
    }

    private var props: Map<String, Any>? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    override val name: String
        get() = props!![nameProp] as String

    private val url: String
        get() = props!![urlProp] as String

    private val user: String
        get() = props!![userProp] as String

    private val password: String
        get() = props!![passwordProp] as String

    override val source: DataSource
        get() {
            val ds = MysqlDataSource();
            ds.setURL(url);
            ds.user = user
            ds.setPassword(password)

            return ds
        }
}