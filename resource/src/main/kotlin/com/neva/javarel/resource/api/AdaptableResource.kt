package com.neva.javarel.resource.api

abstract class AdaptableResource : Resource {

    override fun <T> adaptTo(clazz: Class<T>): T {
        return resolver.adapt(this, clazz)
    }
}
