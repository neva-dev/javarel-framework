package com.neva.javarel.communication.rest.impl

import com.google.common.collect.Sets
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.felix.scr.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.service.component.ComponentContext
import org.osgi.service.http.HttpService
import java.util.*

@Component(
        immediate = true, metatype = true, policy = ConfigurationPolicy.OPTIONAL,
        label = "REST application", description = "Configure REST components support"
)
@Service
class JerseyRestApplication : RestApplication {

    companion object {
        @Property(
                name = servletPrefixProp, value = "/",
                label = "URI prefix", description = "Prepends path to resource")
        const val servletPrefixProp = "servletPrefix"
    }

    @Reference(referenceInterface = RestComponent::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private val components = Sets.newConcurrentHashSet<RestComponent>()

    @Reference
    private lateinit var router: RestRouter

    @Reference
    private lateinit var httpService: HttpService

    private var servletPrefix: String? = null

    @Activate
    @Modified
    private fun start(ctx: ComponentContext) {
        unregister()
        servletPrefix = ctx.properties.get(servletPrefixProp) as String
        register()
    }

    @Deactivate
    private fun stop() {
        unregister()
    }

    private fun register() {
        var config = ResourceConfig()
        for (resource in components) {
            config.register(resource)
        }
        val servletContainer = ServletContainer(config)
        val props = Hashtable<String, String>()

        if (components.isNotEmpty()) {
            httpService.registerServlet(servletPrefix, servletContainer, props, null)
            router.configure(components)
        }
    }

    private fun unregister() {
        if (servletPrefix != null && components.isNotEmpty()) {
            try {
                httpService.unregister(servletPrefix)
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