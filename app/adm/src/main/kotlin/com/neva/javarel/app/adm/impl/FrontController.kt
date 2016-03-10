package com.neva.javarel.app.adm.impl

import com.neva.javarel.communication.rest.api.Redirect
import com.neva.javarel.communication.rest.api.Rest
import com.neva.javarel.communication.rest.api.RestComponent
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Component(immediate = true)
@Instantiate
@Provides
@Path("/adm")
class FrontController : RestComponent {

    @Path("/")
    @GET
    @Rest(name = "home")
    fun getHome(): Response? {
        return Redirect.to("/adm/system/home");
    }

}