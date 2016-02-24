package com.neva.javarel.storage.api

interface ResourceResolver {

    fun find(uri: String): Resource?

    fun resolve(uri: String): Resource

    fun <T> adapt(resource: Resource, clazz: Class<T>): T

    fun isAdaptable(clazz: Class<Any>): Boolean
}
