package com.neva.javarel.communication.rest.api

interface RestRouter {

    fun configure(components: Set<Class<*>>)

    val routes: Set<RestRoute>

    val aliases: Map<String, String>

    fun routeByAction(action: String): RestRoute

    fun routeByName(name: String): RestRoute
}