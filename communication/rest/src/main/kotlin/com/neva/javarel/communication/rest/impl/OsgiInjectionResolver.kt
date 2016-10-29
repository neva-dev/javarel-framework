package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Osgi
import com.neva.javarel.foundation.api.osgi.OsgiUtils
import org.glassfish.hk2.api.*
import javax.inject.Singleton

@Singleton
class OsgiInjectionResolver : InjectionResolver<Osgi> {

    override fun resolve(injectee: Injectee, root: ServiceHandle<*>?): Any? {
        val service = OsgiUtils(javaClass).serviceOf<Any>(injectee.requiredType.typeName)
        if (service == null && !injectee.isOptional) {
            throw MultiException(UnsatisfiedDependencyException(injectee))
        }

        return service
    }

    override fun isConstructorParameterIndicator(): Boolean {
        return true
    }

    override fun isMethodParameterIndicator(): Boolean {
        return false
    }

}