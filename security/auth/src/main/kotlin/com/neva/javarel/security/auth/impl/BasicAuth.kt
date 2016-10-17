package com.neva.javarel.security.auth.impl

import com.google.common.collect.Sets
import com.neva.javarel.security.auth.api.Auth
import org.apache.felix.scr.annotations.*
import org.apache.shiro.realm.Realm
import org.apache.shiro.realm.SimpleAccountRealm
import org.apache.shiro.web.env.WebEnvironment
import org.apache.shiro.web.mgt.WebSecurityManager
import org.glassfish.grizzly.osgi.httpservice.HttpServiceExtension
import javax.servlet.Filter

@Component(immediate = true)
@Service(Auth::class)
class BasicAuth : Auth {

    @Reference(referenceInterface = Realm::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var realms = Sets.newConcurrentHashSet<Realm>()

    @Reference
    private lateinit var http: HttpServiceExtension

    private var _env: WebEnvironment? = null

    private var filter: Filter? = null

    @Activate
    protected fun activate() {
        val realm = SimpleAccountRealm()
        realm.addAccount("admin", "admin", "*:*")
        realms.add(realm)

        val filter = BasicAuthFilter(realms)
        http.registerFilter(filter, "/*", null, null)

        this._env = filter.env
    }

    protected fun deactivate() {
        http.unregisterFilter(filter)
    }

    private fun bindRealms(realm: Realm) {
        realms.add(realm)
    }

    private fun unbindRealms(realm: Realm) {
        realms.remove(realm)
    }

    override val security: WebSecurityManager
        get() = _env!!.webSecurityManager
}