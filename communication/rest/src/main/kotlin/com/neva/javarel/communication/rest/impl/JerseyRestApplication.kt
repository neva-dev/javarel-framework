package com.neva.javarel.communication.rest.impl

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestComponent
import org.apache.felix.ipojo.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.ops4j.pax.web.service.WebContainer
import java.util.*

@Component(immediate = true)
@Instantiate
@Provides
class JerseyRestApplication : RestApplication {

    companion object {
        val servletPrefix = "/*"
    }

    private val registeredComponents = Sets.newConcurrentHashSet<RestComponent>()

    @Requires
    lateinit var webContainer: WebContainer

    @Bind(aggregate = true)
    fun bindResource(component: RestComponent) {
        registeredComponents.add(component)
        update()
    }

    @Unbind
    fun unbindResource(component: RestComponent) {
        registeredComponents.remove(component)
        update()
    }

    override fun update() {
        synchronized(this) {
            if (registeredComponents.isNotEmpty()) {
                try {
                    webContainer.unregister(servletPrefix)
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
                webContainer.registerServlet(servletPrefix, servletContainer, props, webContainer.defaultSharedHttpContext)
            }
        }
    }

    override val components: Set<RestComponent>
        get() = ImmutableSet.copyOf(registeredComponents)
}