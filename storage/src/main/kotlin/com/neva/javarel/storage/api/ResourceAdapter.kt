package com.neva.javarel.storage.api

interface ResourceAdapter {

    val type: Class<Any>

    fun adapt(resource: Resource): Any
}
