package com.neva.javarel.security.auth.impl

import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Properties
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.apache.shiro.realm.SimpleAccountRealm
import org.apache.shiro.web.env.DefaultWebEnvironment
import org.apache.shiro.web.env.EnvironmentLoader
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.servlet.ShiroFilter
import javax.servlet.Filter

@Service(Filter::class)
@Component(immediate = true)
@Properties(
        Property(name = "service.ranking", value= "100"),
        Property(name = "pattern", value = ".*")
)
class BasicAuthFilter : ShiroFilter {

    constructor() : super() {
        // intentionally empty
    }

    override fun init() {
        val realm = SimpleAccountRealm()
        realm.addAccount("admin", "admin", "*:*")

        val env = DefaultWebEnvironment()
        env.securityManager = DefaultWebSecurityManager(realm)
        env.filterChainResolver = PathMatchingFilterChainResolver()

        servletContext.setAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY, env)

        super.init()
    }

    override fun destroy() {
        super.destroy()

        servletContext.removeAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY)
    }

}