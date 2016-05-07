package com.neva.javarel.app.adm

import com.neva.javarel.app.adm.system.SystemController
import com.neva.javarel.communication.rest.api.Redirect
import com.neva.javarel.communication.rest.api.Route
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.communication.rest.api.Uses
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/")
class FrontController {

    @Uses
    private lateinit var urlGenerator: UrlGenerator

    @GET
    @Route(name = "root")
    fun getRoot(): Response {
        return Redirect.to(urlGenerator.action(FrontController::getHome))
    }

    @Path("/adm")
    @GET
    @Route(name = "home")
    fun getHome(): Response {
        return Redirect.to(urlGenerator.action(SystemController::getDashboard))
    }

}