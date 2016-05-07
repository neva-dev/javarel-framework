package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.OsgiService
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestRegistrar
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.server.ResourceConfig

import javax.inject.Singleton

class OsgiResourceConfig(registrars: Set<RestRegistrar>, components: Set<RestComponent>) : ResourceConfig() {

    init {
        for (registrar in registrars) {
            registrar.register(this)
        }
        for (resource in components) {
            register(resource)
        }

        register(object : AbstractBinder() {
            override fun configure() {
                bind(OsgiInjectionResolver::class.java).to(object : TypeLiteral<InjectionResolver<OsgiService>>() {

                }).`in`(Singleton::class.java)
            }
        })
    }
}