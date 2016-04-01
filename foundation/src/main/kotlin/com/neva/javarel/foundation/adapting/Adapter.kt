package com.neva.javarel.foundation.adapting

import kotlin.reflect.KClass

/**
 * Factory which transforms concrete type of adaptee into another.
 */
interface Adapter<Source, Target : Any> {

    val type: KClass<Target>

    fun adapt(source: Source): Target
}
