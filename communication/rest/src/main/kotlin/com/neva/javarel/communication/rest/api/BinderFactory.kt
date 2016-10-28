package com.neva.javarel.communication.rest.api

import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.ScopedBindingBuilder
import org.glassfish.jersey.process.internal.RequestScoped
import kotlin.reflect.KClass
import org.glassfish.hk2.utilities.binding.AbstractBinder as BaseBinder

abstract class BinderFactory<T : Any> : BaseBinder(), Factory<T> {

    override fun dispose(instance: T) {
        // nothing to do
    }

    protected fun bindPerRequest(clazz: KClass<T>): ScopedBindingBuilder<T>? {
        return bindFactory(javaClass)
                .to(clazz.java)
                .proxy(true)
                .proxyForSameScope(false)
                .`in`(RequestScoped::class.java)
    }

}