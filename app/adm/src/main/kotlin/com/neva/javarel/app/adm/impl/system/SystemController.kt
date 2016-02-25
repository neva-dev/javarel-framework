package com.neva.javarel.app.adm.impl.system

import com.neva.javarel.app.core.api.controller.BaseController
import com.neva.javarel.communication.rest.api.RestResource
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Component
@Instantiate
@Provides(specifications = arrayOf(RestResource::class))
@Path("/adm/system")
class SystemController : BaseController() {

    @GET
    @Path("/home")
    fun getHome(): Response {
        return Response.ok("System welcomes!").build()
    }

}