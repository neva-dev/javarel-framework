package com.neva.javarel.security.auth.impl

import com.google.common.collect.Sets
import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.security.auth.api.*
import org.apache.felix.scr.annotations.*

@Component(
        immediate = true,
        label = "${JavarelConstants.SERVICE_PREFIX} Multi Realm Auth"
)
@Service(Auth::class)
class MultiRealmAuth : Auth {

    @Reference
    private lateinit var config: AuthConfig

    @Reference(
            referenceInterface = Realm::class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private var allRealms = Sets.newConcurrentHashSet<Realm>()

    override fun supports(credentials: Credentials): Boolean {
        return allRealms.any { realm -> realm.supports(credentials) }
    }

    override fun byCredentials(credentials: Credentials): Authenticable? {
        var authenticable: Authenticable? = null

        val realm = allRealms.firstOrNull { realm -> realm.supports(credentials) }
        if (realm != null) {
            authenticable = realm.byCredentials(credentials)
        }

        return authenticable
    }

    override val realms: Set<Realm>
        get() = allRealms

    override val guest: Authenticable
        get() {
            val guest = byCredentials(PrincipalCredentials(config.guestPrincipal))

            return guest ?: throw AuthException("At least one realm must can authenticate using credentials with principal '${config.guestPrincipal}'")
        }

    private fun bindAllRealms(realm: Realm) {
        allRealms.add(realm)
    }

    private fun unbindAllRealms(realm: Realm) {
        allRealms.remove(realm)
    }

}