package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestResource
import org.apache.felix.ipojo.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.service.http.HttpService
import java.util.*

@Component(immediate = true)
@Instantiate
class RestApplication : ResourceConfig() {

    companion object {
        // TODO let it be parameterizable
        val servletPrefix = "/"
    }

    @Requires
    lateinit var httpService: HttpService

    @Requires(specification = RestResource::class)
    lateinit var resources: List<RestResource>

    @Validate
    fun validate() {
        // TODO check why only one resource is being registered
        for (resource in resources) {
            register(resource)
        }

        val servletContainer = ServletContainer(this)
        val props = Hashtable<String, String>()

        httpService.registerServlet(servletPrefix, servletContainer, props, null);
    }

    @Invalidate
    fun invalidate() {
        httpService.unregister(servletPrefix)
    }

}