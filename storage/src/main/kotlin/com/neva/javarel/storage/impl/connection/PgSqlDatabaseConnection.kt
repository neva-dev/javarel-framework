package com.neva.javarel.storage.impl.connection

import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.storage.api.DatabaseConnection
import org.apache.commons.lang3.StringUtils
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

@Component(immediate = true, configurationFactory = true, metatype = true, label = "${JavarelConstants.servicePrefix} Storage - PostgreSQL Connection")
@Service
class PgSqlDatabaseConnection : DatabaseConnection {

    companion object {
        @Property(name = nameProp, value = "pgsql", label = "Connection name", description = "Unique identifier")
        const val nameProp = "name"

        @Property(name = hostProp, value = "localhost", label = "Host", description = "Hostname or IP address to server")
        const val hostProp = "hostProp"

        @Property(name = portProp, value = "5432", label = "Port", description = "Port number")
        const val portProp = "portProp"

        @Property(name = dbNameProp, value = "javarel", label = "Database name")
        const val dbNameProp = "dbNameProp"

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

    private val host: String
        get() = props!![hostProp] as String

    private val port: Int
        get() = Integer.parseInt(props!![portProp] as String)

    private val dbName: String
        get() = props!![dbNameProp] as String

    private val user: String
        get() = props!![userProp] as String

    private val password: String
        get() = props!![passwordProp] as String

    override val source: DataSource
        get() {
            val ds = PGSimpleDataSource()

            ds.setServerName(host);
            ds.setDatabaseName(dbName);
            ds.setPortNumber(port);

            if (!StringUtils.isBlank(user)) {
                ds.user = user;
            }
            if (!StringUtils.isBlank(password)) {
                ds.password = password;
            }

            return ds
        }
}