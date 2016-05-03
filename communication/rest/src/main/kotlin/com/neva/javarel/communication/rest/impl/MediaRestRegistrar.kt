package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestRegistrar
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig

/**
 * Add support for JSON, XML etc..
 *
 * TODO XML in JAX-RS
 */
@Component(immediate = true)
@Service
class MediaRestRegistrar : RestRegistrar {

    override fun register(config: ResourceConfig) {
        config.register(JacksonFeature::class)
    }

}