package com.neva.javarel.security.auth.impl

import com.neva.javarel.communication.rest.api.AbstractBinder
import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.security.auth.api.Session
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context

@Binder
class RequestSessionBinder : AbstractBinder<Session>() {

    @Context
    private lateinit var request: HttpServletRequest

    override fun configure() {
        bindPerRequest(Session::class)
    }

    override fun provide(): Session {
        return RequestSession(request.session)
    }

}