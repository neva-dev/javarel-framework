package com.neva.presentation.asset.impl

import com.neva.javarel.communication.rest.api.RestResource
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Component(immediate = true)
@Instantiate
@Provides
@Path("/asset")
class AssetResource : RestResource {

    @Path("/read")
    @GET
    fun getRead(): Response? {
        return Response.ok("Assets world!").build()
    }

}