package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.foundation.api.osgi.OsgiUtils
import org.glassfish.hk2.api.Injectee
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.ServiceHandle
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.binding.AbstractBinder
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Binder
@Singleton
class OsgiInjectionResolver : InjectionResolver<Uses>, AbstractBinder() {

    @Inject
    @Named(InjectionResolver.SYSTEM_RESOLVER_NAME)
    private lateinit var systemResolver: InjectionResolver<Inject>

    override fun resolve(injectee: Injectee, root: ServiceHandle<*>?): Any? {
        var service = OsgiUtils(javaClass).serviceOf<Any>(injectee.requiredType.typeName)
        if (service == null) {
            service = systemResolver.resolve(injectee, root)
        }

        return service
    }

    override fun isMethodParameterIndicator(): Boolean {
        return true
    }

    override fun isConstructorParameterIndicator(): Boolean {
        return true
    }

    override fun configure() {
        bind(javaClass).to(object : TypeLiteral<InjectionResolver<Uses>>() {}).`in`(Singleton::class.java)
    }
}