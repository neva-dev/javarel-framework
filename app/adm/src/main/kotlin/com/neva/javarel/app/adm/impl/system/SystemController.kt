package com.neva.javarel.app.adm.impl.system

import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Component(immediate = true)
@Instantiate
@Provides(specifications = arrayOf(RestComponent::class))
@Path("/adm/system")
class SystemController : RestComponent {

    @Requires
    private lateinit var resourceResolver: ResourceResolver

    @GET
    @Path("/home")
    fun getHome(): Response {
        val html = resourceResolver.resolve("bundle://adm/view/system/home.peb")
                .adaptTo(View::class)
                .render()

        return Response.ok(html)
                .type("text/html")
                .build()
    }

}