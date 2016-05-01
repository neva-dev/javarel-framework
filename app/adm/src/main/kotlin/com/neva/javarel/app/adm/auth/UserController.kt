package com.neva.javarel.app.adm.system

import com.neva.javarel.app.adm.auth.User
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.storage.api.Persister
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
    private lateinit var persister: Persister

    @GET
    @Path("/create")
    fun getCreate(): Response {
        val emf = persister.getEntityManagerFactory("storage")
        val em = emf.createEntityManager()

        em.transaction.begin()

        val user = User(RandomStringUtils.randomAscii(8), Date())
        em.persist(user)
        em.flush()

        em.transaction.commit()

        return Response.ok("User '${user.name}' created with ID: ${user.id}")
                .type(MediaType.TEXT_PLAIN)
                .build()
    }

}