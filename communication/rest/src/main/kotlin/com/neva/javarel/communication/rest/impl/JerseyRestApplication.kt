package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestConfig
import com.neva.javarel.communication.rest.api.RestRouter
import com.neva.javarel.foundation.api.scanning.BundleScanner
import com.neva.javarel.foundation.api.scanning.BundleWatcher
import com.neva.javarel.foundation.api.scanning.ComponentScanBundleFilter
import org.apache.felix.http.api.ExtHttpService
import org.apache.felix.scr.annotations.*
import org.glassfish.jersey.servlet.ServletContainer
import org.glassfish.jersey.servlet.ServletProperties
import org.osgi.framework.BundleEvent
import org.slf4j.LoggerFactory
import java.util.*
import javax.ws.rs.ApplicationPath
import javax.ws.rs.Path
import javax.ws.rs.ext.Provider

@Component(immediate = true)
@Service
class JerseyRestApplication : RestApplication, BundleWatcher {

    companion object {
        val LOG = LoggerFactory.getLogger(JerseyRestApplication::class.java)
        val COMPONENT_FILTER = ComponentScanBundleFilter(setOf(
                Path::class.java,
                Provider::class.java,
                ApplicationPath::class.java,
                Binder::class.java
        ))
    }

    @Reference
    private lateinit var bundleScanner: BundleScanner

    @Reference
    private lateinit var router: RestRouter

    @Reference
    private lateinit var http: ExtHttpService

    @Reference
    private lateinit var restConfig: RestConfig

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

    private var filter: ServletContainer? = null

    private fun start() {
        try {
            LOG.debug("Starting REST application.")

            val components = bundleScanner.scan(COMPONENT_FILTER)
            val resourceConfig = OsgiResourceConfig(components)
            resourceConfig.properties = mapOf(ServletProperties.FILTER_CONTEXT_PATH to "/")
            this.filter = JerseyFilter(restConfig, resourceConfig)

            http.registerFilter(filter, ".*", filterProps, JerseyFilter.RANKING, null)
            router.configure(components)
            started = true
        } catch (e: Throwable) {
            LOG.debug("REST application cannot be started properly.", e)
        }
    }

    private fun stop() {
        try {
            LOG.debug("Stopping REST application.")

            if (filter != null) {
                http.unregisterFilter(filter)
            }
        } catch (e: Throwable) {
            LOG.debug("REST application cannot be stopped properly.", e)
        }
    }

    private val filterProps: Dictionary<String, Any>
        get() {
            val props = Hashtable<String, Any>()
            props.put(ServletProperties.FILTER_CONTEXT_PATH, "")

            return props
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