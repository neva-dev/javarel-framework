package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.Osgi
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.binding.AbstractBinder
import javax.inject.Singleton

@Binder
class RestBinder : AbstractBinder() {

    override fun configure() {
        bind(OsgiInjectionResolver::class.java).to(object : TypeLiteral<InjectionResolver<Osgi>>() {}).`in`(Singleton::class.java)
    }

}