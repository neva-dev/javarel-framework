package com.neva.javarel.communication.rest.impl

import org.apache.felix.ipojo.annotations.*
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.service.http.HttpService
import java.util.*

@Component(immediate = true)
@Instantiate
class RestApplication : ResourceConfig() {

    companion object {
        val PREFIX = "/jersey"
    }

    @Requires
    lateinit var httpService: HttpService

    @Validate
    fun validate() {

        registerClasses(SampleResource::class.java)
        val servletContainer = ServletContainer(this)

        httpService.registerServlet(PREFIX, servletContainer, Hashtable<String, String>(), null);
    }

    @Invalidate
    fun invalidate() {
        httpService.unregister(PREFIX)
    }

}