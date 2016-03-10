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
class RestController : RestComponent {

    @Requires
    private lateinit var router: RestRouter

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getList(): Response? {
        val resources = router.routes.fold(mutableListOf<Resource>(), { acc, route ->
            acc.add(Resource(route.name, route.methods, route.path, route.action, route.parameters)); acc;
        });

        return Response.ok(Gson().toJson(resources)).build()
    }

    data class Resource(val name: String?, val methods: Collection<String>, val uri: String,
                        val action: String, val parameters: Collection<String>)

}