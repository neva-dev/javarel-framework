package com.neva.javarel.communication.rest.impl

import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JerseyFilter(resourceConfig: ResourceConfig) : ServletContainer(resourceConfig) {

    companion object {
        val RANKING = 100
    }

    /**
     * TODO make patterns configurable somehow
     */
    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (request.requestURI.startsWith("/system/console")) {
            chain.doFilter(request, response)
        } else {
            super.doFilter(request, response, chain)
        }
    }
}