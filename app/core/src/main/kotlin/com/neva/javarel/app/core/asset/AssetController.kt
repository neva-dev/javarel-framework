package com.neva.javarel.app.core.asset

import com.neva.javarel.communication.rest.api.Rest
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Component
@Service
@Path("/asset")
class AssetController : RestComponent {

    @Reference
    private lateinit var resolver: ResourceResolver

    @GET
    @Path("/{path:.+}")
    @Rest(name = "asset")
    fun getOrigin(@PathParam("path") path: String): Response {
        val asset = resolveAsset(path)

        return Response.ok(asset.read()).type(asset.mimeType).build()
    }

    @GET
    @Path("/compiled/{path:.+}")
    @Rest(name = "asset.compiled")
    fun getCompiled(@PathParam("path") path: String): Response {
        val asset = resolveAsset(path)

        return Response.ok(asset.compile()).type(asset.mimeType).build()
    }

    private fun resolveAsset(path: String): Asset {
        return resolver.findOrFail(path).adaptTo(Asset::class)
    }

}