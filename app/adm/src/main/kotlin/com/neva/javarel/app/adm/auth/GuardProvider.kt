package com.neva.javarel.app.adm.auth

import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.security.auth.api.AuthenticableProvider
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.security.auth.api.guard.RequestGuard
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.process.internal.RequestScoped
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context

@Binder
class GuardProvider : AbstractBinder(), Factory<Guard> {

    @Uses
    private lateinit var userProvider: AuthenticableProvider

    @Context
    private lateinit var request: HttpServletRequest

    override fun provide(): Guard {
        return RequestGuard(request, userProvider)
    }

    override fun dispose(instance: Guard) {
        // nothing to do
    }

    override fun configure() {
        bindFactory(GuardProvider::class.java).to(Guard::class.java).`in`(RequestScoped::class.java)
    }

}