package com.neva.javarel.communication.rest.api

interface RestRouter {

    val routes: Set<RestRoute>

    fun routeByAction(action: String): RestRoute

    fun routeByUri(uri: String): RestRoute
}