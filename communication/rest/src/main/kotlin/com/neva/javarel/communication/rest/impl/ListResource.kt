package com.neva.javarel.communication.rest.impl

import com.google.gson.Gson
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component(immediate = true)
@Instantiate
@Provides
@Path("/rest")
class ListResource : RestComponent {

    @Requires
    private lateinit var restRouter: RestRouter

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getList(): Response? {
        return Response.ok(Gson().toJson(restRouter.routes)).build()
    }

}