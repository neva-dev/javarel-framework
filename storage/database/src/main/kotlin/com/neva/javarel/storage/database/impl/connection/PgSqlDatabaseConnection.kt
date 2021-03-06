package com.neva.javarel.storage.database.impl.connection

import com.neva.javarel.foundation.api.JavarelConstants
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

@Component(immediate = true, configurationFactory = true, metatype = true, label = "${JavarelConstants.SERVICE_PREFIX} Storage - PostgreSQL Connection")
@Service
class PgSqlDatabaseConnection : AbstractDatabaseConnection() {

    companion object {
        @Property(name = NAME_PROP, value = "pgsql", label = "Connection name", description = "Unique identifier")
        const val NAME_PROP = "name"

        @Property(name = HOST_PROP, value = "localhost", label = "Host", description = "Server IP address or hostname")
        const val HOST_PROP = "hostProp"

        @Property(name = PORT_PROP, value = "5432", label = "Port", description = "Port number")
        const val PORT_PROP = "portProp"

        @Property(name = DB_NAME_PROP, value = "javarel", label = "Database name")
        const val DB_NAME_PROP = "dbNameProp"

        @Property(name = USER_PROP, value = "root", label = "Username")
        const val USER_PROP = "userProp"

        @Property(name = PASSWORD_PROP, value = "toor", label = "Password")
        const val PASSWORD_PROP = "passwordProp"
    }

    private lateinit var props: Map<String, Any>

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    override val name: String
        get() = props[NAME_PROP] as String

    private val host: String
        get() = props[HOST_PROP] as String

    private val port: Int
        get() = Integer.parseInt(props[PORT_PROP] as String)

    private val dbName: String
        get() = props[DB_NAME_PROP] as String

    private val user: String
        get() = props[USER_PROP] as String

    private val password: String
        get() = props[PASSWORD_PROP] as String

    override val source: DataSource
        get() {
            val ds = PGSimpleDataSource()

            ds.setServerName(host);
            ds.setDatabaseName(dbName);
            ds.setPortNumber(port);

            if (!user.isNullOrBlank()) {
                ds.user = user;
            }
            if (!password.isNullOrBlank()) {
                ds.password = password;
            }

            return ds
        }
}