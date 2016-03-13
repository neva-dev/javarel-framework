package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestRouter
import com.neva.javarel.communication.rest.api.UrlGenerator
import org.apache.commons.lang3.StringUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

import javax.ws.rs.core.Response
import kotlin.reflect.KFunction1

@Component(immediate = true)
@Service
class GenericUrlGenerator : UrlGenerator {

    @Reference
    private lateinit var router: RestRouter

    @Reference
    private lateinit var config: JerseyRestConfig

    override fun action(action: KFunction1<*, Response>): String {
        return action(action, emptyMap())
    }

    override fun action(action: KFunction1<*, Response>, params: Map<String, Any>): String {
        return action(StringUtils.substringBetween(action.toString(), "fun ", "():"), params)
    }

    override fun action(action: String): String {
        return action(action, emptyMap())
    }

    override fun action(action: String, params: Map<String, Any>): String {
        val route = router.routeByAction(action)

        return JerseyRestRoute.mergePath(config.uriPrefix, route.assembleUri(params))
    }

    override fun name(name: String): String {
        return name(name, emptyMap())
    }

    override fun name(name: String, params: Map<String, Any>): String {
        val route = router.routeByName(name)

        return JerseyRestRoute.mergePath(config.uriPrefix, route.assembleUri(params))
    }

}