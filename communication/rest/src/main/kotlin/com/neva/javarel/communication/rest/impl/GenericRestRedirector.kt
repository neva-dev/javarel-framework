package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestRedirector
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.commons.lang3.StringUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import java.net.URI
import javax.ws.rs.core.Response
import kotlin.reflect.KFunction1

@Component(immediate = true)
@Service
class GenericRestRedirector : RestRedirector {

    @Reference
    private lateinit var router: RestRouter

    override fun toAction(action: KFunction1<*, Response>): Response {
        return toAction(action, emptyMap())
    }

    override fun toAction(action: KFunction1<*, Response>, params: Map<String, Any>): Response {
        return toAction(StringUtils.substringBetween(action.toString(), "fun ", "():"), params)
    }

    override fun toAction(action: String, params: Map<String, Any>): Response {
        val route = router.routeByAction(action)
        val uri = URI(route.assembleUri(params))

        return Response.seeOther(uri).build()
    }

}