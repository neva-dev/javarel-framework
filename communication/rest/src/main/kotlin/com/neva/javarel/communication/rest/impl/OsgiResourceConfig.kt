package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.OsgiService
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.server.ResourceConfig
import javax.inject.Singleton

class OsgiResourceConfig(components: Set<Class<*>>) : ResourceConfig() {

    init {
        register(object : AbstractBinder() {
            override fun configure() {
                bind(OsgiInjectionResolver::class.java).to(object : TypeLiteral<InjectionResolver<OsgiService>>() {

                }).`in`(Singleton::class.java)
            }
        })
        for (component in components) {
            register(component)
        }
    }
}