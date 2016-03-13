package com.neva.javarel.communication.rest.impl

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestComponent
import org.apache.felix.scr.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.framework.BundleContext
import org.osgi.service.http.HttpService
import java.util.*

@Component(
        immediate = true, metatype = true, policy = ConfigurationPolicy.OPTIONAL,
        label = "REST application", description = "Configure REST components support"
)
@Service
class JerseyRestApplication : RestApplication {

    companion object {
        const val servletPrefixProp = "servletPrefix"
    }

    @Reference(referenceInterface = RestComponent::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
    private val registeredComponents = Sets.newConcurrentHashSet<RestComponent>()

    @Reference
    private lateinit var httpService: HttpService

    @Property(
            name = servletPrefixProp, value = "/",
            label = "URI prefix", description = "Prepends path to resource")
    private var servletPrefix: String? = null

    @Activate
    protected fun start(ctx: BundleContext, props: Map<String, Any>) {
        servletPrefix = props.get(servletPrefixProp) as String
        update()
    }

    override fun update() {
        synchronized(this) {
            if (registeredComponents.isNotEmpty()) {
                try {
                    httpService.unregister(servletPrefix)
                } catch (e: Throwable) {
                    // nothing interesting
                }
            }

            var config = ResourceConfig()
            for (resource in registeredComponents) {
                config.register(resource)
            }
            val servletContainer = ServletContainer(config)
            val props = Hashtable<String, String>()

            if (registeredComponents.isNotEmpty()) {
                httpService.registerServlet(servletPrefix, servletContainer, props, null)
            }
        }
    }

    override val components: Set<RestComponent>
        get() = ImmutableSet.copyOf(registeredComponents)

    protected fun bindRestComponent(component: RestComponent) {
        registeredComponents.add(component)
    }

    protected fun unbindRestComponent(component: RestComponent) {
        registeredComponents.remove(component)
    }
}