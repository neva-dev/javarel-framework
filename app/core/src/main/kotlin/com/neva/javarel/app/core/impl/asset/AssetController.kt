package com.neva.javarel.app.core.impl.asset

import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceMapper
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
    fun getOrigin(@PathParam("path") path: String): Response {
        val resource = getResource(path)
        val asset = resource.adaptTo(Asset::class)

        return Response.ok(asset.read()).type(asset.mimeType).build()
    }

    @GET
    @Path("/compiled/{path:.+}")
    fun getCompiled(@PathParam("path") path: String): Response {
        val resource = getResource(path)
        val asset = resource.adaptTo(Asset::class)

        return Response.ok(asset.compile()).type(asset.mimeType).build()
    }

    private fun getResource(path: String): Resource {
        return resolver.resolve(ResourceMapper.pathToUri(path))
    }
}
