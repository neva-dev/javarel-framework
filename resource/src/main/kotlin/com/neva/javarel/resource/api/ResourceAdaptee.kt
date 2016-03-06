package com.neva.javarel.resource.api

import java.io.InputStream
import kotlin.reflect.KClass

abstract class ResourceAdaptee(protected val resource: Resource) : Resource {

    override val resolver: ResourceResolver
        get() = resource.resolver

    override val descriptor: ResourceDescriptor
        get() = resource.descriptor

    override val inputStream: InputStream
        get() = resource.inputStream

    override fun <T : Any> adaptTo(clazz: KClass<T>): T {
        return resource.adaptTo(clazz)
    }
}
