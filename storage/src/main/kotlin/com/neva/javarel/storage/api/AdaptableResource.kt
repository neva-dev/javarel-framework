package com.neva.javarel.storage.api

abstract class AdaptableResource : Resource {

    override fun <T> `as`(clazz: Class<T>): T {
        return resolver.adapt(this, clazz)
    }
}
