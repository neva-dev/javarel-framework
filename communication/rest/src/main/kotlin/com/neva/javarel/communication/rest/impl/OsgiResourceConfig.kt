package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Binder
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory

class OsgiResourceConfig(components: Set<Class<*>>) : ResourceConfig() {

    companion object {
        val LOG = LoggerFactory.getLogger(OsgiResourceConfig::class.java)
    }

    init {
        for (component in components) {
            if (component.isAnnotationPresent(Binder::class.java)) {
                try {
                    LOG.debug("Registering REST binder '${component.canonicalName}'")
                    register(component.newInstance() as AbstractBinder)
                } catch (e: Exception) {
                    LOG.warn("Cannot register REST binder '${component.canonicalName}'. Probably it is not extending '${AbstractBinder::class.java.canonicalName}'", e)
                }
            } else {
                LOG.debug("Registering REST component '${component.canonicalName}'")
                register(component)
            }
        }
    }
}