package com.neva.javarel.security.auth.impl

import com.google.common.collect.Sets
import com.neva.javarel.security.auth.api.Auth
import org.apache.felix.scr.annotations.*
import org.apache.shiro.realm.Realm
import org.apache.shiro.realm.SimpleAccountRealm
import org.apache.shiro.web.env.WebEnvironment
import org.apache.shiro.web.mgt.WebSecurityManager
import org.osgi.framework.BundleContext
import org.osgi.service.http.HttpService
import java.util.*
import javax.servlet.Filter

@Component(immediate = true)
@Service(Auth::class)
class BasicAuth : Auth {

    @Reference(referenceInterface = Realm::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var realms = Sets.newConcurrentHashSet<Realm>()

    @Reference
    private lateinit var http: HttpService

    private var _env: WebEnvironment? = null

    private var filter: Filter? = null

    @Activate
    protected fun activate(bundleContext: BundleContext) {
        val realm = SimpleAccountRealm()
        realm.addAccount("admin", "admin", "*:*")
        realms.add(realm)

        val filter = BasicAuthFilter(realms)


        val props = Hashtable<String, Any>()
       // val urls = arrayOf("/foo", "/protected")
        props.put("filter-name", filter.javaClass.name)
        props.put("urlPatterns", "/*")
        bundleContext.registerService(Filter::class.java.name, filter, props)
        //  http.registerFilter(filter, "/*", null, null)

        this._env = filter.env
    }

    @Deactivate
    protected fun deactivate() {
        //   http.unregisterFilter(filter)
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