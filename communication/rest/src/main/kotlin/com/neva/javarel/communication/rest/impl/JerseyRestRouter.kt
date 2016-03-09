package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestApplication
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
            val routes = emptySet<RestRoute>();

            restApp.components.forEach {
                val resource = Resource.from(it.javaClass)

                resource.resourceMethods.forEach {
                    val uri = "${it.httpMethod} ..."

                    routes + RestRoute(uri)
                }
            }

            return routes
        }

}