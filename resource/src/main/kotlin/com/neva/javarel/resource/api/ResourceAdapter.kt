package com.neva.javarel.resource.api

interface ResourceAdapter {

    val type: Class<Any>

    fun adapt(resource: Resource): Any
}
