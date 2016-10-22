package com.neva.javarel.security.auth.impl

import com.neva.javarel.communication.rest.api.AbstractBinder
import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.security.auth.api.AuthenticableProvider
import com.neva.javarel.security.auth.api.Guard
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context

@Binder
class GuardProvider : AbstractBinder<Guard>() {

    @Uses
    private lateinit var auth: AuthenticableProvider

    @Context
    private lateinit var request: HttpServletRequest

    override fun configure() {
        bindPerRequest(Guard::class)
    }

    override fun provide(): Guard {
        return RequestGuard(request, auth)
    }

}