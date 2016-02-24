package com.neva.javarel.storage.api

import java.io.InputStream

abstract class ResourceAdaptee(protected val resource: Resource) : Resource {

    override val resolver: ResourceResolver
        get() = resource.resolver

    override val descriptor: ResourceDescriptor
        get() = resource.descriptor

    override val inputStream: InputStream
        get() = resource.inputStream

    override fun <T> `as`(clazz: Class<T>): T {
        return resource.`as`(clazz)
    }
}
