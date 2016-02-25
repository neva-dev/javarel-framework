package com.neva.javarel.app.adm.impl.system

import com.google.common.base.Joiner
import com.neva.javarel.communication.rest.api.RestComponent
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.slf4j.LoggerFactory
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Component
@Instantiate
@Provides(specifications = arrayOf(RestComponent::class))
@Provider
class UncaughtException : Throwable(), ExceptionMapper<Throwable>, RestComponent {

    companion object {
        val logger = LoggerFactory.getLogger(UncaughtException::class.java)
    }

    override fun toResponse(e: Throwable): Response {
        logger.error("Uncaught exception!", e)

        return Response.status(500)
                .entity("${Joiner.on("\n").join(ExceptionUtils.getRootCauseStackTrace(e))}")
                .type("text/plain")
                .build()
    }
}