package com.neva.javarel.app.adm.auth

import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.security.auth.impl.RequestGuard
import org.glassfish.hk2.api.Factory
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context
import javax.ws.rs.ext.Provider

@Provider
class GuardProvider : Factory<Guard> {

    @Uses
    private lateinit var userRepository : UserRepository

    @Context
    private lateinit var request : HttpServletRequest

    override fun provide(): Guard? {
        return RequestGuard(request, userRepository)
    }

    override fun dispose(instance: Guard?) {
        // nothing to do
    }

}