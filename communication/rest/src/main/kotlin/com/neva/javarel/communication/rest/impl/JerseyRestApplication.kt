package com.neva.javarel.communication.rest.impl

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestComponent
import org.apache.felix.ipojo.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.ops4j.pax.web.service.WebContainer
import java.util.*

@Component(immediate = true)
@Instantiate
class JerseyRestApplication : RestApplication {

    companion object {
        val servletPrefix = "/javarel"
    }

    private var components = Lists.newCopyOnWriteArrayList<RestComponent>()

    @Requires
    lateinit var webContainer: WebContainer

    @Bind(aggregate = true)
    fun bindResource(component: RestComponent) {
        components.add(component)
        updateHttpService()
    }

    @Unbind
    fun unbindResource(component: RestComponent) {
        components.remove(component)
        updateHttpService()
    }

    private fun updateHttpService() {
        synchronized(this) {
            if (components.isNotEmpty()) {
                try {
                    webContainer.unregister(servletPrefix)
                } catch (e: Throwable) {
                    // nothing interesting
                }
            }

            var config = ResourceConfig()
            for (resource in components) {
                config.register(resource)
            }
            val servletContainer = ServletContainer(config)
            val props = Hashtable<String, String>()

            if (components.isNotEmpty()) {
                webContainer.registerServlet(servletPrefix, servletContainer, props, null)
            }
        }
    }

    override fun getComponents(): Collection<RestComponent> {
        return ImmutableList.copyOf(components);
    }

}