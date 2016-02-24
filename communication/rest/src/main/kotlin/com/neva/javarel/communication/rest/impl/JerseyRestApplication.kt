package com.neva.javarel.communication.rest.impl

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestResource
import org.apache.felix.ipojo.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.service.http.HttpService
import java.util.*

@Component(immediate = true)
@Instantiate
class JerseyRestApplication : RestApplication {

    companion object {
        val servletPrefix = "/"
    }

    @Requires
    lateinit var httpService: HttpService

    private var resources = Lists.newCopyOnWriteArrayList<RestResource>()

    @Bind(aggregate = true)
    fun bindResource(resource: RestResource) {
        resources.add(resource)
        updateHttpService()
    }

    @Unbind
    fun unbindResource(resource: RestResource) {
        resources.remove(resource)
        updateHttpService()
    }

    private fun updateHttpService() {
        synchronized(this) {
            if (resources.isNotEmpty()) {
                httpService.unregister(servletPrefix)
            }

            var config = ResourceConfig()
            for (resource in resources) {
                config.register(resource)
            }
            val servletContainer = ServletContainer(config)
            val props = Hashtable<String, String>()

            httpService.registerServlet(servletPrefix, servletContainer, props, null);
        }
    }

    override fun getResources(): Collection<RestResource> {
        return ImmutableList.copyOf(resources);
    }

}