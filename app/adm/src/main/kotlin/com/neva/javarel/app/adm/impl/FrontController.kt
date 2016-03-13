package com.neva.javarel.app.adm.impl

import com.neva.javarel.app.adm.impl.system.SystemController
import com.neva.javarel.communication.rest.api.Redirect
import com.neva.javarel.communication.rest.api.Rest
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.UrlGenerator
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import javax.ws.rs.GET

import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Component
@Service
@Path("/")
class FrontController : RestComponent {

    @Reference
    private lateinit var urlGenerator: UrlGenerator

    @GET
    @Rest(name = "root")
    fun getRoot(): Response {
        return Redirect.to(urlGenerator.action(FrontController::getHome))
    }

    @Path("/adm")
    @GET
    @Rest(name = "home")
    fun getHome(): Response {
        return Redirect.to(urlGenerator.action(SystemController::getHome))
    }

}