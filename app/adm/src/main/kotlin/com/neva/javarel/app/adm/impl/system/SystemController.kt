package com.neva.javarel.app.adm.impl.system

import com.neva.javarel.communication.rest.api.RestResource
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Component
@Instantiate
@Provides(specifications = arrayOf(RestResource::class))
@Path("/adm/system")
class SystemController : RestResource {

    // TODO not initialized
    @Requires(optional = true)
    lateinit var resourceResolver: ResourceResolver

    @GET
    @Path("/home")
    fun getHome(): Response {
        return Response.ok("System welcomes!").build()
    }

}