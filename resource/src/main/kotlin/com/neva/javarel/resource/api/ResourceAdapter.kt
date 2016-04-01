package com.neva.javarel.resource.api

import com.neva.javarel.foundation.api.adapting.Adapter
import kotlin.reflect.KClass

abstract class ResourceAdapter<T : Any> : Adapter<Resource, T> {

    override val sourceTypes: Set<KClass<Resource>>
        get() = setOf(Resource::class)

}