package com.neva.javarel.app.adm.system

import com.neva.javarel.app.adm.auth.User
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.storage.api.DatabaseAdmin
import org.apache.commons.lang3.RandomStringUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Service(RestComponent::class)
@Component(immediate = true)
@Path("/adm/auth/user")
class UserController : RestComponent {

    @Reference
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