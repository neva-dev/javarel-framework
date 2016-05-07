package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestRouter
import com.neva.javarel.foundation.api.osgi.BundleScanner
import com.neva.javarel.foundation.api.osgi.BundleWatcher
import org.apache.felix.scr.annotations.*
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.framework.BundleEvent
import org.osgi.service.http.HttpService
import java.util.*

/**
 * TODO update HTTP server using batch of component changes between e.g 1 sec / postpone
 */
@Component(immediate = true, policy = ConfigurationPolicy.OPTIONAL)
@Service
class JerseyRestApplication : RestApplication, BundleWatcher {

    companion object {
        val componentFilter = ComponentFilter()
    }

    @Reference
    private lateinit var bundleScanner: BundleScanner

    @Reference
    private lateinit var httpService: HttpService

    @Reference
    private lateinit var config: JerseyRestConfig

    @Reference
    private lateinit var router: RestRouter

    private var ready = false

    private var started = false

    @Activate
    fun activate() {
        ready = true
        toggle(true)
    }

    @Deactivate
    fun deactivate() {
        toggle(false)
    }

    @Synchronized
    private fun start() {
        val components = bundleScanner.scan(componentFilter)
        var resourceConfig = OsgiResourceConfig(components)
        val servletContainer = ServletContainer(resourceConfig)
        val props = Hashtable<String, String>()

        httpService.registerServlet(config.uriPrefix, servletContainer, props, null)
        router.configure(components)
        started = true
    }

    private fun stop() {
        try {
            httpService.unregister(config.uriPrefix)
        } catch (e: Throwable) {
            // nothing interesting
        }
    }

    @Synchronized
    override fun toggle(start: Boolean) {
        if (ready) {
            if (started) {
                stop()
            }

            if (start) {
                start()
            }
        }
    }

    override fun watch(event: BundleEvent) {
        if (componentFilter.filterBundle(event.bundle)) {
            toggle(true)
        }
    }
}