package com.neva.javarel.security.auth.impl

import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.security.auth.api.AuthConfig
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
@Service(AuthConfig::class)
class DefaultAuthConfig : AuthConfig {

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
                description = "Name of account with super user privileges."
        )
        const val ADMIN_PRINCIPAL = "adminPrincipal"
    }

    private var props: Map<String, Any>? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    override val guestPrincipal: String
        get() = props!!.get(GUEST_PRINCIPAL) as String

    override val adminPrincipal: String
        get() = props!!.get(ADMIN_PRINCIPAL) as String

}