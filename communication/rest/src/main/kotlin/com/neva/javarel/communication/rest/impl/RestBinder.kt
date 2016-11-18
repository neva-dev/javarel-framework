package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.AbstractBinder
import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.foundation.api.injection.Osgi
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral

@Binder
class RestBinder : AbstractBinder() {

    override fun configure() {
        bindSingleton(OsgiInjectionResolver::class, object : TypeLiteral<InjectionResolver<Osgi>>() {})
    }

}