package com.neva.javarel.communication.rest.impl

import com.google.common.collect.Sets
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.felix.scr.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.service.http.HttpService
import java.util.*

@Component(immediate = true, policy = ConfigurationPolicy.OPTIONAL)
@Service
class JerseyRestApplication : RestApplication {

    @Reference(referenceInterface = RestComponent::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
    private val components = Sets.newConcurrentHashSet<RestComponent>()

    @Reference
    private lateinit var httpService: HttpService

    @Reference
    private lateinit var config: JerseyRestConfig

    @Reference
    private lateinit var router: RestRouter

    @Activate
    private fun start() {
        var resourceConfig = ResourceConfig()
        for (resource in components) {
            resourceConfig.register(resource)
        }
        val servletContainer = ServletContainer(resourceConfig)
        val props = Hashtable<String, String>()

        if (components.isNotEmpty()) {
            httpService.registerServlet(config.uriPrefix, servletContainer, props, null)
            router.configure(components)
        }
    }

    @Deactivate
    private fun stop() {
        if (components.isNotEmpty()) {
            try {
                httpService.unregister(config.uriPrefix)
            } catch (e: Throwable) {
                // nothing interesting
            }
        }
    }

    private fun bindRestComponent(component: RestComponent) {
        components.add(component)
    }

    private fun unbindRestComponent(component: RestComponent) {
        components.remove(component)
    }
}