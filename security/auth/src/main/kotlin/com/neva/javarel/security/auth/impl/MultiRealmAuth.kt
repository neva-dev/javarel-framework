package com.neva.javarel.security.auth.impl

import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.security.auth.api.*
import org.apache.felix.scr.annotations.*
import java.util.*

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
    private var allRealms = Collections.synchronizedSortedSet(TreeSet<Realm>({ r1, r2 -> r2.priority.compareTo(r1.priority) }))

    override fun supports(credentials: Credentials): Boolean {
        return allRealms.any { realm -> realm.supports(credentials) }
    }

    override fun byCredentials(credentials: Credentials): Authenticable? {
        val realms = allRealms.filter { realm -> realm.supports(credentials) }
        for (realm in realms) {
            val authenticable = realm.byCredentials(credentials)
            if (authenticable != null) {
                return authenticable
            }
        }

        return null
    }

    override val realms: Set<Realm>
        get() = allRealms

    override val guest: Authenticable
        get() = byCredentials(PrincipalCredentials(config.guest.principal)) ?: config.guest

    override val priority: Int
        get() = throw UnsupportedOperationException("Multi auth realm aggregates another realms so it is not using priority in any kind.")

    private fun bindAllRealms(realm: Realm) {
        allRealms.add(realm)
    }

    private fun unbindAllRealms(realm: Realm) {
        allRealms.remove(realm)
    }

}