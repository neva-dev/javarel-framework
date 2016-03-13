package com.neva.javarel.app.adm.impl.dev

import com.google.gson.Gson
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.felix.scr.annotations.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Service
@Path("/adm/dev")
class RestController : RestComponent {

    @Volatile
    @Reference(policy = ReferencePolicy.DYNAMIC)
    private lateinit var router: RestRouter

    @Path("/rest/list")
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