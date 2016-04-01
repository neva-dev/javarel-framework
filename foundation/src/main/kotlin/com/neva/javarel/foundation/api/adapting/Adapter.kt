package com.neva.javarel.foundation.api.adapting

import kotlin.reflect.KClass

/**
 * Factory which transforms concrete type of adaptee into another.
 */
interface Adapter<S : Any, T : Any> {

    val sourceTypes: Set<KClass<S>>

    val targetType: KClass<T>

    fun adapt(adaptable: S): T
}
