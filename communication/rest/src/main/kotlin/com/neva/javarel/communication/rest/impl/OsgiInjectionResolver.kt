package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.OsgiService
import org.glassfish.hk2.api.Injectee
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.ServiceHandle
import org.osgi.framework.FrameworkUtil
import javax.inject.Inject
import javax.inject.Named

class OsgiInjectionResolver : InjectionResolver<OsgiService> {

    @Inject @Named(InjectionResolver.SYSTEM_RESOLVER_NAME)
    private lateinit var systemResolver: InjectionResolver<Inject>

    override fun resolve(injectee: Injectee, root: ServiceHandle<*>?): Any? {
        val bundleContext = FrameworkUtil.getBundle(javaClass).bundleContext
        val reference = bundleContext.getServiceReference(injectee.requiredType.typeName)
        if (reference != null) {
            val service = bundleContext.getService(reference)
            if (service != null) {
                return service
            }
        }

        return systemResolver.resolve(injectee, root)
    }

    override fun isMethodParameterIndicator(): Boolean {
        return true
    }

    override fun isConstructorParameterIndicator(): Boolean {
        return false
    }
}