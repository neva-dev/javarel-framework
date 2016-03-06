package com.neva.javarel.resource.api

import kotlin.reflect.KClass

interface ResourceResolver {

    fun find(uri: String): Resource?

    fun resolve(uri: String): Resource

    fun <T : Any> adapt(resource: Resource, clazz: KClass<T>): T

    fun isAdaptable(clazz: KClass<Any>): Boolean
}
