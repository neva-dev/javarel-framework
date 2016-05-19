package com.neva.javarel.app.adm.auth

import com.neva.javarel.communication.rest.api.Redirect
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.security.auth.api.Credentials
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.storage.api.DatabaseAdmin
import org.apache.commons.lang3.RandomStringUtils
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/adm/auth/user")
class UserController {

    @Uses
    private lateinit var db: DatabaseAdmin

    @Uses
    private lateinit var guard: Guard

    @Uses
    private lateinit var urlGenerator: UrlGenerator

    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    fun getCreate(): User {
        return db.session { em ->
            val repo = UserRepository(em)
            val user = User("ciapunek@gmail.com", "test123", RandomStringUtils.randomAscii(8), Date())

            repo.save(user)

            return@session user
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    fun getList(): List<User> {
        return db.session { em ->
            val repo = UserRepository(em)
            val users = repo.findAll()

            return@session users.toList()
        }
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    fun getLogin(): Response {
        if (guard.attempt(Credentials())) {
            return Redirect.to(urlGenerator.action("home"))
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

}