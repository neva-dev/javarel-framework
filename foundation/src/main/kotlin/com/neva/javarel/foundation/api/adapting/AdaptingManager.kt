package com.neva.javarel.foundation.api.adapting

import kotlin.reflect.KClass

/**
 * Adapting centre
 */
interface AdaptingManager : Adapting<Any> {

    val adapters: Map<KClass<Any>, Set<Adapter<Any, Any>>>

}