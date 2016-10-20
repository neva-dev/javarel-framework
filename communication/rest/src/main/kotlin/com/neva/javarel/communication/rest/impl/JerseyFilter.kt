package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestConfig
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JerseyFilter(private val restConfig: RestConfig, resourceConfig: ResourceConfig) : ServletContainer(resourceConfig) {

    companion object {
        val RANKING = 100
    }

    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (shouldFilter(request)) {
            super.doFilter(request, response, chain)
        } else {
            chain.doFilter(request, response)
        }
    }

    private fun shouldFilter(request: HttpServletRequest) = restConfig.filters.none { it.value(request) }
}