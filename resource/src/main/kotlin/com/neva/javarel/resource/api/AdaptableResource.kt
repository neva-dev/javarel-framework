package com.neva.javarel.resource.api

import kotlin.reflect.KClass

abstract class AdaptableResource : Resource {

    override fun <T : Any> adaptTo(clazz: KClass<T>): T {
        return resolver.adapt(this, clazz)
    }
}
