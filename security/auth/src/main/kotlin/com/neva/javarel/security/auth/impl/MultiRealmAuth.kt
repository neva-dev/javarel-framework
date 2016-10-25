package com.neva.javarel.security.auth.impl

import com.google.common.collect.Sets
import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.security.auth.api.*
import org.apache.felix.scr.annotations.*

@Component(
        metatype = true,
        label = "${JavarelConstants.SERVICE_PREFIX} Auth",
        description = "Configuration for auth and realms."
)
@Service(Auth::class)
class MultiRealmAuth : Auth {

    companion object {
        @Property(
                name = GUEST_PRINCIPAL,
                value = "guest",
                label = "Guest principal",
                description = "Name of account which will be used to find authenticable when current user is not authenticated."
        )
        const val GUEST_PRINCIPAL = "guestPrincipal"
    }

    @Reference(referenceInterface = Realm::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var allRealms = Sets.newConcurrentHashSet<Realm>()

    private var props: Map<String, Any>? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

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
            val guest = byCredentials(PrincipalCredentials(guestPrincipal))

            return guest ?: throw AuthException("At least one realm must can authenticate using credentials with principal '$guestPrincipal'")
        }

    override val guestPrincipal: String
        get() = props!!.get(GUEST_PRINCIPAL) as String

    private fun bindAllRealms(realm: Realm) {
        allRealms.add(realm)
    }

    private fun unbindAllRealms(realm: Realm) {
        allRealms.remove(realm)
    }

}