package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestRegistrar
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import javax.ws.rs.ext.Provider

/**
 * Add support for JSON, XML etc..
 *
 * TODO XML in JAX-RS
 */
@Provider
class MediaRestRegistrar : RestRegistrar {

    override fun register(config: ResourceConfig) {
        config.register(JacksonFeature::class)
    }

}