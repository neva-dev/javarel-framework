package com.neva.javarel.app.adm.system

import com.neva.javarel.app.adm.auth.User
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.foundation.api.media.json.Json
import com.neva.javarel.storage.api.DatabaseAdmin
import org.apache.commons.lang3.RandomStringUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Service(RestComponent::class)
@Component(immediate = true)
@Path("/adm/auth/user")
class UserController : RestComponent {

    @Reference
    private lateinit var dbAdmin: DatabaseAdmin

    @GET
    @Path("/create")
    fun getCreate(): Response {
        val user = dbAdmin.database().session { em ->
            val user = User(RandomStringUtils.randomAscii(8), Date())
            em.persist(user)
            em.flush()

            return@session user
        }

        return Response.ok(Json.serialize(user), MediaType.APPLICATION_JSON_TYPE).build()
    }

    @GET
    @Path("/list")
    fun getList(): Response {
        val users: List<User> = dbAdmin.database().session { em ->
            return@session em.createQuery("SELECT u FROM User u", User::class.java).getResultList();
        }

        return Response.ok(Json.serialize(users), MediaType.APPLICATION_JSON_TYPE).build()
    }

}