package com.neva.javarel.resource.api

import java.io.InputStream

interface Resource {

    val resolver: ResourceResolver

    val descriptor: ResourceDescriptor

    val inputStream: InputStream

    fun <T> adaptTo(clazz: Class<T>): T
}
