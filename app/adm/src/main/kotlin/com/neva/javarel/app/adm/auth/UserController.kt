package com.neva.javarel.app.adm.auth

import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.storage.api.DatabaseAdmin
import org.apache.commons.lang3.RandomStringUtils
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/adm/auth/user")
class UserController {

    @Uses
    private lateinit var db: DatabaseAdmin

    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    fun getCreate(): User {
        return db.session { em ->
            val user = User(RandomStringUtils.randomAscii(8), Date())
            em.persist(user)
            em.flush()

            return@session user
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    fun getList(): List<User> {
        return db.session { em ->
            return@session em.createQuery("SELECT u FROM User u", User::class.java).getResultList();
        }
    }

}