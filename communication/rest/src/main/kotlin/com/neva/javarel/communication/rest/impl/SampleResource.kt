package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestResource
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/sample")
class SampleResource : RestResource {

    @Path("/hello")
    @GET
    fun getHello(): Response? {
        return Response.ok("Hello World!").build()
    }

}