package com.neva.javarel.communication.rest.api

import javax.servlet.http.HttpServletRequest

interface RestConfig {

    /**
     * Set of filters which prevents handling request via REST application.
     */
    val filters: Map<String, (HttpServletRequest) -> Boolean>

    /**
     * Register new filter preventing request handling via REST application.
     */
    fun registerFilter(name: String, predicate: (HttpServletRequest) -> Boolean)

    /**
     * Unregister existing filter preventing request handling via REST application.
     */
    fun unregisterFilter(name: String)

}