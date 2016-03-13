package com.neva.javarel.communication.rest.api

interface RestRouter {

    fun configure(components: Set<RestComponent>)

    val routes: Set<RestRoute>

    fun routeByAction(action: String): RestRoute

    fun routeByName(name: String): RestRoute
}