package com.neva.javarel.resource.api

import kotlin.reflect.KClass

interface ResourceAdapter<T : Any> {

    val type: KClass<T>

    fun adapt(resource: Resource): T
}
