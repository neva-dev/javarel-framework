package com.neva.javarel.app.adm.impl.dev

import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestRouter
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.ReferencePolicy
import org.apache.felix.scr.annotations.Service
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Service
@Path("/adm/dev/rest")
class RestController : RestComponent {

    @Reference
    private lateinit var resourceResolver: ResourceResolver

    @Reference
    private lateinit var router: RestRouter

    @Path("/routes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getRoutes(): Response? {
        val html = resourceResolver.findOrFail("bundle://adm/view/dev/rest/routes.peb")
                .adaptTo(View::class)
                .with("routes", router.routes)
                .render()

        return Response.ok(html)
                .type(MediaType.TEXT_HTML)
                .build()
    }

}