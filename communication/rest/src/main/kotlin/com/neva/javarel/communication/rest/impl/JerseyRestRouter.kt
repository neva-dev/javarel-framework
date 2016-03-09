package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestException
import com.neva.javarel.communication.rest.api.RestRoute
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import org.glassfish.jersey.server.model.Resource

@Component(immediate = true)
@Instantiate
@Provides
class JerseyRestRouter : RestRouter {

    @Requires
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

    override fun routeByUri(uri: String): RestRoute {
        return routeBy({ it.uri == uri }, "Route cannot be found by URI '$uri'")
    }

}