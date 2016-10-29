package com.neva.javarel.communication.rest.api

import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.jersey.process.internal.RequestScoped
import javax.inject.Singleton
import kotlin.reflect.KClass
import org.glassfish.hk2.utilities.binding.AbstractBinder as BaseBinder

abstract class AbstractBinder : BaseBinder() {

    protected fun bindRequestScoped(class1: KClass<*>, class2: KClass<*>) {
        bind(class1.java).to(class2.java).`in`(RequestScoped::class.java).proxy(true).proxyForSameScope(false)
    }

    protected fun bindSingleton(class1: KClass<*>, class2: KClass<*>) {
        bind(class1.java).to(class2.java).`in`(Singleton::class.java)
    }

    protected fun bindSingleton(class1: KClass<*>, class2: TypeLiteral<*>) {
        bind(class1.java).to(class2).`in`(Singleton::class.java)
    }

}