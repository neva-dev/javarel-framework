package com.neva.javarel.app.core.impl.asset

import com.neva.javarel.communication.rest.api.Rest
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Component(immediate = true)
@Provides
@Instantiate
@Path("/asset")
class AssetController : RestComponent {

    @Requires
    lateinit var resolver: ResourceResolver

    @GET
    @Path("/{path:.+}")
    @Rest(name = "asset")
    fun getOrigin(@PathParam("path") path: String): Response {
        val asset = resolver.resolve(path).adaptTo(Asset::class)

        return Response.ok(asset.read()).type(asset.mimeType).build()
    }

    @GET
    @Path("/compiled/{path:.+}")
    @Rest(name = "asset.compiled")
    fun getCompiled(@PathParam("path") path: String): Response {
        val asset = resolver.resolve(path).adaptTo(Asset::class)

        return Response.ok(asset.compile()).type(asset.mimeType).build()
    }

}
