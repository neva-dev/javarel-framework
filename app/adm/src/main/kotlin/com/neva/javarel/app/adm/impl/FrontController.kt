package com.neva.javarel.app.adm.impl

import com.neva.javarel.app.adm.impl.system.SystemController
import com.neva.javarel.communication.rest.api.Rest
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestRedirector
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Component(immediate = true)
@Instantiate
@Provides
@Path("/")
class FrontController : RestComponent {

    @Requires
    private lateinit var redirector: RestRedirector

    @GET
    @Rest(name = "root")
    fun getRoot(): Response {
        return redirector.toAction(FrontController::getHome)
    }

    @Path("/adm")
    @GET
    @Rest(name = "home")
    fun getHome(): Response {
        return redirector.toAction(SystemController::getHome)
    }

}