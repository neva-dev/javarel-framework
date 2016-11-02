package com.neva.javarel.storage.store.impl

import com.mongodb.MongoClient
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.storage.store.api.StoreConnection
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service

@Component(
        immediate = true,
        configurationFactory = true,
        metatype = true,
        label = "${JavarelConstants.SERVICE_PREFIX} Storage - Store Connection"
)
@Service
class MultiStoreConnection : StoreConnection {

    companion object {
        const val NAME_DEFAULT = "default"

        @Property(name = NAME_PROP, value = "default", label = "Connection name", description = "Unique identifier")
        const val NAME_PROP = "name"

        @Property(name = HOST_PROP, value = "localhost", label = "Host", description = "Server IP address or hostname")
        const val HOST_PROP = "hostProp"

        @Property(name = PORT_PROP, value = "27017", label = "Port", description = "Port number")
        const val PORT_PROP = "portProp"

        @Property(name = DB_NAME_PROP, value = "javarel", label = "Database name")
        const val DB_NAME_PROP = "dbNameProp"

        @Property(name = USER_PROP, value = "", label = "Username")
        const val USER_PROP = "userProp"

        @Property(name = PASSWORD_PROP, value = "", label = "Password")
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

    override val dbName: String
        get() = props[DB_NAME_PROP] as String

    private val user: String
        get() = props[USER_PROP] as String

    private val password: String
        get() = props[PASSWORD_PROP] as String

    override val client: MongoClient
        get() {
            val address = ServerAddress(host, port)

            val credentials = mutableListOf<MongoCredential>()
            if (!user.isNullOrBlank()) {
                credentials.add(MongoCredential.createCredential(user, dbName, password.toCharArray()))
            }

            return MongoClient(address, credentials)
        }
}