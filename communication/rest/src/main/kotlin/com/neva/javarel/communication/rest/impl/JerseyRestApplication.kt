package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestRouter
import com.neva.javarel.foundation.api.scanning.BundleScanner
import com.neva.javarel.foundation.api.scanning.BundleWatcher
import org.apache.felix.scr.annotations.*
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.framework.BundleEvent
import org.osgi.service.http.HttpService
import org.slf4j.LoggerFactory
import java.util.*

@Component(immediate = true, policy = ConfigurationPolicy.OPTIONAL)
@Service
class JerseyRestApplication : RestApplication, BundleWatcher {

    companion object {
        val LOG = LoggerFactory.getLogger(JerseyRestApplication::class.java)
        val COMPONENT_FILTER = ComponentBundleFilter()
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

    private fun start() {
        try {
            LOG.debug("Starting REST application.")

            val components = bundleScanner.scan(COMPONENT_FILTER)
            var resourceConfig = OsgiResourceConfig(components)
            val servletContainer = ServletContainer(resourceConfig)
            val props = Hashtable<String, String>()

            httpService.registerServlet(config.uriPrefix, servletContainer, props, null)
            router.configure(components)
            started = true
        } catch (e: Throwable) {
            LOG.debug("REST application cannot be started properly.", e)
        }

    }

    private fun stop() {
        try {
            LOG.debug("Stopping REST application.")

            httpService.unregister(config.uriPrefix)
        } catch (e: Throwable) {
            LOG.debug("REST application cannot be stopped properly.", e)
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
        if (COMPONENT_FILTER.filterBundle(event.bundle)) {
            toggle(true)
        }
    }
}