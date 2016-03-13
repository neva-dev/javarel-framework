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


    private val registeredComponents = Sets.newConcurrentHashSet<RestComponent>()

    @Requires
    lateinit var httpService: HttpService

    @Requires
    lateinit var config: JerseyRestConfig

    private var usedUriPrefix: String? = null

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

    @Updated
    override fun update() {
        synchronized(this) {
            if (usedUriPrefix != null) {
                try {
                    httpService.unregister(usedUriPrefix)
                } catch (e: Throwable) {
                    // nothing interesting
                }
            }

            var resourceConfig = ResourceConfig()
            for (resource in registeredComponents) {
                resourceConfig.register(resource)
            }
            val servletContainer = ServletContainer(resourceConfig)
            val props = Hashtable<String, String>()

            if (registeredComponents.isNotEmpty()) {
                usedUriPrefix = config.uriPrefix
                httpService.registerServlet(config.uriPrefix, servletContainer, props, null)
            }
        }
    }

    override val components: Set<RestComponent>
        get() = ImmutableSet.copyOf(registeredComponents)
}