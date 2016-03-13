package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestException
import com.neva.javarel.communication.rest.api.RestRoute
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.glassfish.jersey.server.model.Resource

@Component(immediate = true)
@Service
class JerseyRestRouter : RestRouter {

    @Reference
    private lateinit var restApp: RestApplication

    override val routes: Set<RestRoute>
        get() {
            val routes = mutableSetOf<RestRoute>()

            restApp.components.forEach { component ->
                val resource = Resource.from(component.javaClass)

                if (resource != null) {
                    resource.childResources.forEach { method ->
                        routes.add(JerseyRestRoute(resource, method))
                    }
                }
            }

            return routes
        }

    // TODO support 'Route-Action-Alias'
    private fun routeBy(predicate: (RestRoute) -> Boolean, notFoundMessage: String): RestRoute {
        routes.forEach { route ->
            if (predicate.invoke(route)) {
                return route;
            }
        }

        throw RestException(notFoundMessage)
    }

    override fun routeByAction(action: String): RestRoute {
        return routeBy({ it.action == action }, "Route cannot be found by action '$action'")
    }

    override fun routeByName(name: String): RestRoute {
        return routeBy({ it.name == name }, "Route cannot be found by name '$name'")
    }

}