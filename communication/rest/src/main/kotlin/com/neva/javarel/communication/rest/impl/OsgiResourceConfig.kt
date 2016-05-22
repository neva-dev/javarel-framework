package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.Uses
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.server.ResourceConfig
import javax.inject.Singleton

class OsgiResourceConfig(components: Set<Class<*>>) : ResourceConfig() {

    init {
        register(object : AbstractBinder() {
            override fun configure() {
                bind(OsgiInjectionResolver::class.java).to(object : TypeLiteral<InjectionResolver<Uses>>() {}).`in`(Singleton::class.java)
            }
        })

        for (component in components) {
            if (component.isAnnotationPresent(Binder::class.java)) {
                val binder = component.newInstance() as AbstractBinder
                register(binder)
            } else {
                register(component)
            }
        }
    }
}