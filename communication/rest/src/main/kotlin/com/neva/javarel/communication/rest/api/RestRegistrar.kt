package com.neva.javarel.communication.rest.api

import org.glassfish.jersey.server.ResourceConfig

/**
 * Configure REST application by registering resources
 */
interface RestRegistrar {

    fun register(config: ResourceConfig)

}