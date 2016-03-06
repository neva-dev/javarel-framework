package com.neva.javarel.communication.rest.impl

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestComponent
import org.apache.felix.ipojo.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.service.http.HttpService
import java.util.*

@Component(immediate = true)
@Instantiate
class JerseyRestApplication : RestApplication {

    companion object {
        val servletPrefix = "/javarel"
    }

    @Requires
    lateinit var httpService: HttpService

    private var components = Lists.newCopyOnWriteArrayList<RestComponent>()

//    @Bind(aggregate = true)
//    fun bindResource(component: RestComponent) {
//        components.add(component)
//        updateHttpService()
//    }
//
//    @Unbind
//    fun unbindResource(component: RestComponent) {
//        components.remove(component)
//        updateHttpService()
//    }

    @Validate
    fun validate() {
        var config = ResourceConfig()
        config.register(ListResource())

        val servletContainer = ServletContainer(config)
        val props = Hashtable<String, String>()
        val context = httpService.createDefaultHttpContext()

        httpService.registerServlet(servletPrefix, servletContainer, props, context)
    }

    private fun updateHttpService() {
        synchronized(this) {
            if (components.isNotEmpty()) {
                try {
                    httpService.unregister(servletPrefix)
                } catch (e: Throwable) {
                    // nothing to do
                }
            }

            var config = ResourceConfig()
            for (resource in components) {
                config.register(resource)
            }
            val servletContainer = ServletContainer(config)
            val props = Hashtable<String, String>()

            if (components.isNotEmpty()) {
                try {
                    httpService.registerServlet(servletPrefix, servletContainer, props, null)
                } catch (e: Throwable) {
                    // nothing to do
                }

            }
        }
    }

    override fun getComponents(): Collection<RestComponent> {
        return ImmutableList.copyOf(components);
    }

}