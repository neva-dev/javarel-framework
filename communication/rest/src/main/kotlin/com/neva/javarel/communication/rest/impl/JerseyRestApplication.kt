package com.neva.javarel.communication.rest.impl

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestComponent
import org.apache.felix.ipojo.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.service.http.HttpService
import java.util.*

@Component(immediate = true)
@Instantiate
@Provides
class JerseyRestApplication : RestApplication {

    companion object {
        val servletPrefix = "/"
    }

    private val registeredComponents = Sets.newConcurrentHashSet<RestComponent>()

    @Requires
    lateinit var httpService: HttpService

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
}