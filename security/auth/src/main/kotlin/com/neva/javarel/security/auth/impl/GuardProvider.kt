package com.neva.javarel.security.auth.impl

import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.security.auth.api.AuthenticableProvider
import com.neva.javarel.security.auth.api.Guard
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.process.internal.RequestScoped
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context

@Binder
class GuardProvider : AbstractBinder(), Factory<Guard> {

    @Uses
    private lateinit var auth: AuthenticableProvider

    @Context
    private lateinit var request: HttpServletRequest

    override fun configure() {
        bindFactory(javaClass).to(Guard::class.java).proxy(true).proxyForSameScope(false).`in`(RequestScoped::class.java)
    }

    override fun provide(): Guard {
        return RequestGuard(request, auth)
    }

    override fun dispose(instance: Guard) {
        // nothing to do
    }

}