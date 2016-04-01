package com.neva.javarel.foundation.api.adapting

import kotlin.reflect.KClass

/**
 * Service which can use one of known adapters to transform specified adaptee into another.
 */
interface Adapting<S: Any> {

    fun <T : Any> adapt(adaptable: S, clazz: KClass<T>): T

}