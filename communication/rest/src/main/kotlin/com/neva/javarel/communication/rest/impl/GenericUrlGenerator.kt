package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestRoute
import com.neva.javarel.communication.rest.api.RestRouter
import com.neva.javarel.communication.rest.api.UrlGenerator
import org.apache.commons.lang3.StringUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

import javax.ws.rs.core.Response
import kotlin.reflect.KFunction1

@Component
@Service
class GenericUrlGenerator : UrlGenerator {

    companion object {
        val HASH_PARAM = "#"
    }

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
        return assemble(router.routeByAction(action), params)
    }

    override fun name(name: String): String {
        return name(name, emptyMap())
    }

    override fun name(name: String, params: Map<String, Any>): String {
        return assemble(router.routeByName(name), params)
    }

    override fun assemble(route: RestRoute, params: Map<String, Any>): String {
        val paramsWithoutHash = mutableMapOf<String, Any>()
        paramsWithoutHash.putAll(params)

        val hash = paramsWithoutHash.remove(HASH_PARAM) as String?
        var url = JerseyRestRoute.mergePath(config.uriPrefix, route.assembleUri(paramsWithoutHash))

        if (StringUtils.isNotBlank(hash)) {
            url += "#$hash"
        }

        return url
    }

}