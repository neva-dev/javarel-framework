package com.neva.javarel.foundation.adapting

import kotlin.reflect.KClass

/**
 * Service which can use one of known adapters to transform specified adaptee into another.
 */
interface AdapterFactory<Source : Any> {

    fun <Target : Any> adapt(adaptable: Source, clazz: KClass<Target>): Target

    fun isAdaptable(clazz: KClass<Any>): Boolean

}