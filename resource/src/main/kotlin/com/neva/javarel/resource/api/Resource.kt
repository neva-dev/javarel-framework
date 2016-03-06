package com.neva.javarel.resource.api

import java.io.InputStream
import kotlin.reflect.KClass

interface Resource {

    val resolver: ResourceResolver

    val descriptor: ResourceDescriptor

    val inputStream: InputStream

    fun <T : Any> adaptTo(clazz: KClass<T>): T
}
