package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.BinderFactory
import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.RestRequest
import org.glassfish.jersey.process.internal.RequestScoped
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context

@Binder
class RestRequestBinder : BinderFactory<RestRequest>() {

    @Context
    private lateinit var request: HttpServletRequest

    override fun configure() {
        bindFactory(javaClass)
                .to(RestRequest::class.java).proxy(true)
                .proxyForSameScope(false)
                .`in`(RequestScoped::class.java)
    }

    override fun provide(): RestRequest {
        return DefaultRestRequest(request)
    }

}