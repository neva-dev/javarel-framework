package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestException
import com.neva.javarel.communication.rest.api.RestRoute
import com.neva.javarel.communication.rest.api.RestRouter
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.foundation.api.http.QueryParams
import org.apache.commons.lang3.StringUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import kotlin.reflect.KFunction1

@Component(immediate = true)
@Service
class GenericUrlGenerator : UrlGenerator {

    @Reference
    private lateinit var router: RestRouter

    // Function reference

    override fun action(action: KFunction1<*, *>): String {
        return action(action, emptyMap())
    }

    override fun action(action: KFunction1<*, *>, params: Map<String, Any>): String {
        return action(actionFromFunction(action), params)
    }

    override fun action(action: KFunction1<*, *>, params: List<Any>): String {
        return action(actionFromFunction(action), params)
    }

    // Fully qualified class and method

    override fun action(action: String): String {
        return action(action, emptyMap())
    }

    override fun action(action: String, params: Map<String, Any>): String {
        return assemble(router.routeByAction(action), params)
    }

    override fun action(action: String, params: List<Any>): String {
        return assemble(router.routeByAction(action), params)
    }

    // Annotated with name

    override fun name(name: String): String {
        return name(name, emptyMap())
    }

    override fun name(name: String, params: Map<String, Any>): String {
        return assemble(router.routeByName(name), params)
    }

    override fun name(name: String, params: List<Any>): String {
        return assemble(router.routeByName(name), params)
    }

    // Implementation

    private fun actionFromFunction(action: KFunction1<*, *>) = StringUtils.substringBetween(action.toString(), "fun ", "():")

    private fun combineParams(route: RestRoute, values: List<Any>): Map<String, Any> {
        if (values.size != route.parameters.size) {
            throw RestException("Route ${route.action} expects ${route.parameters.size} parameter(s) to be specified, but ${values.size} given.")
        }

        val params = mutableMapOf<String, Any>()
        for (i in values.indices) {
            params.put(route.parameters[i], values[i])
        }

        return params
    }

    private fun assemble(route: RestRoute, params: List<Any>): String {
        return assemble(route, combineParams(route, params))
    }

    private fun assemble(route: RestRoute, params: Map<String, Any>): String {
        val pathParams = mutableMapOf<String, Any>()
        val queryParams = mutableMapOf<String, Any>()

        for ((name, value) in params) {
            if (route.parameters.contains(name)) {
                pathParams[name] = value
            } else {
                queryParams[name] = value
            }
        }

        return route.assemble(pathParams) + QueryParams.fromMap(queryParams).toString()
    }

}