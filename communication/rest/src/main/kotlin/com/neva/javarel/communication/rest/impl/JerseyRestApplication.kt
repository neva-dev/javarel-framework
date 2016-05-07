package com.neva.javarel.communication.rest.impl

import com.google.common.collect.Sets
import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestRegistrar
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.felix.scr.annotations.*
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.service.http.HttpService
import java.util.*

/**
 * TODO update HTTP server using batch of component changes between e.g 1 sec / postpone
 */
@Component(immediate = true, policy = ConfigurationPolicy.OPTIONAL)
@Service
class JerseyRestApplication : RestApplication {

    @Reference(referenceInterface = RestComponent::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private val components = Sets.newConcurrentHashSet<RestComponent>()

    @Reference(referenceInterface = RestRegistrar::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private val registrars = Sets.newConcurrentHashSet<RestRegistrar>()

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
        var resourceConfig = OsgiResourceConfig(registrars, components)
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

    private fun bindRestComponent(component: RestComponent) {
        components.add(component)
        toggle(true)
    }

    private fun unbindRestComponent(component: RestComponent) {
        components.remove(component)
        toggle(true)
    }

    private fun bindRestRegistrar(registrar: RestRegistrar) {
        registrars.add(registrar)
        toggle(true)
    }

    private fun unbindRestRegistrar(registrar: RestRegistrar) {
        registrars.remove(registrar)
        toggle(true)
    }
}