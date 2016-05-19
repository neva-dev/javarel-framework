package com.neva.javarel.app.adm.auth

import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.security.auth.api.guard.RequestGuard
import org.glassfish.hk2.api.Factory
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context
import javax.ws.rs.ext.Provider

// TODO https://codahale.com/what-makes-jersey-interesting-injection-providers/ fix UserController
@Provider
class GuardProvider : Factory<Guard> {

    @Uses
    private lateinit var userProvider: UserProvider

    @Context
    private lateinit var request: HttpServletRequest

    override fun provide(): Guard {
        return RequestGuard(request, userProvider)
    }

    override fun dispose(instance: Guard) {
        // nothing to do
    }

}