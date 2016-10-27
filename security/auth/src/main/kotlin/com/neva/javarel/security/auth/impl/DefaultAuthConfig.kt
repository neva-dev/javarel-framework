package com.neva.javarel.security.auth.impl

import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.security.auth.api.*
import org.apache.commons.lang3.RandomStringUtils
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service

@Component(
        immediate = true,
        metatype = true,
        label = "${JavarelConstants.SERVICE_PREFIX} Auth Config",
        description = "Configuration for auth and realms."
)
@Service(Realm::class, AuthConfig::class)
class DefaultAuthConfig : BasicRealm(), AuthConfig {

    companion object {
        @Property(
                name = GUEST_PRINCIPAL,
                value = "guest",
                label = "Guest principal",
                description = "Name of account which will be used to find authenticable when current user is not authenticated."
        )
        const val GUEST_PRINCIPAL = "guestPrincipal"

        @Property(
                name = ADMIN_PRINCIPAL,
                value = "admin",
                label = "Admin principal",
                description = "Name of account which will be used for user with super user privileges."
        )
        const val ADMIN_PRINCIPAL = "adminPrincipal"

        @Property(
                name = ADMIN_PASSWORD,
                value = "admin",
                label = "Admin password",
                description = "Name of account which will be used for user with super user privileges."
        )
        const val ADMIN_PASSWORD = "adminPassword"

        const val PRIORITY = 0
    }

    private lateinit var props: Map<String, Any>

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    override val priority: Int
        get() = PRIORITY

    override fun byCredentials(credentials: Credentials): Authenticable? {
        return when (credentials) {
            is PrincipalCredentials -> {
                return authenticables[credentials.principal]
            }
            is PrincipalPasswordCredentials -> {
                var result: Authenticable? = null

                val authenticable = authenticables[credentials.principal]
                if (authenticable != null && authenticable.password == credentials.password) {
                    result = authenticable
                }

                return result
            }
            else -> null
        }
    }

    val authenticables: Map<String, PrincipalPasswordAuthenticable> by lazy {
        mapOf(
                guest.principal to guest,
                admin.principal to admin
        )
    }

    override val guest: PrincipalPasswordAuthenticable by lazy {
        PrincipalPasswordAuthenticable(
                props.get(GUEST_PRINCIPAL) as String,
                RandomStringUtils.randomAscii(32)
        )
    }

    override val admin: PrincipalPasswordAuthenticable by lazy {
        PrincipalPasswordAuthenticable(
                props.get(ADMIN_PRINCIPAL) as String,
                props.get(ADMIN_PASSWORD) as String
        )
    }

}