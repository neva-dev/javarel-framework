package com.neva.javarel.security.auth.impl

import org.apache.shiro.realm.Realm
import org.apache.shiro.web.env.DefaultWebEnvironment
import org.apache.shiro.web.env.EnvironmentLoader
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.servlet.ShiroFilter

class BasicAuthFilter(realms: Collection<Realm>) : ShiroFilter() {

    val env = DefaultWebEnvironment()

    init {
        env.securityManager = DefaultWebSecurityManager(realms)
        env.filterChainResolver = PathMatchingFilterChainResolver()
    }

    override fun init() {
        servletContext.setAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY, env)

        super.init()
    }

    override fun destroy() {
        super.destroy()

        servletContext.removeAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY)
    }

}