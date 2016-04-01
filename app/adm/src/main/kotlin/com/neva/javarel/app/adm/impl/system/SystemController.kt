package com.neva.javarel.app.adm.impl.system

import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Service
@Path("/adm/system")
class SystemController : RestComponent {

    @Reference
    private lateinit var resourceResolver: ResourceResolver

    @GET
    @Path("/home")
    fun getHome(): Response {
        val html = resourceResolver.findOrFail("bundle://adm/view/system/home.peb")
                .adaptTo(View::class)
                .render()

        return Response.ok(html)
                .type(MediaType.TEXT_HTML)
                .build()
    }

}