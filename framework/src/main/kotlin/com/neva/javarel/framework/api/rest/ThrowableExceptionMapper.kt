package com.neva.javarel.framework.api.rest

import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper

abstract class ThrowableExceptionMapper : ExceptionMapper<Throwable> {

    companion object {
        val LOG = LoggerFactory.getLogger(ThrowableExceptionMapper::class.java)
    }

    abstract fun map(causeException: Throwable): Response

    override fun toResponse(causeException: Throwable): Response {
        try {
            return map(causeException)
        } catch (internalException: Throwable) {
            LOG.error("Internal error occurred while mapping cause exception", internalException)

            return toText(causeException)
        }
    }

    protected fun toText(e: Throwable): Response {
        val text = "${ExceptionUtils.getRootCauseMessage(e)}\n${ExceptionUtils.getRootCauseStackTrace(e).joinToString("\n")}"

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(text)
                .type(MediaType.TEXT_PLAIN)
                .build()
    }
}