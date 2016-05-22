package com.neva.javarel.app.adm.error

import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceNotFoundException
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ThrowableMapper : ExceptionMapper<Throwable> {

    @Uses
    private var resolver: ResourceResolver? = null

    companion object {
        val LOG = LoggerFactory.getLogger(ThrowableMapper::class.java)
    }

    override fun toResponse(causeException: Throwable): Response {
        try {
            when (causeException) {
                is NotFoundException,
                is ResourceNotFoundException -> {
                    LOG.debug("Resource not found", causeException)

                    return respondView(causeException, "bundle://adm/view/error/not-found.peb")
                }
                else -> {
                    LOG.error("Request error", causeException)
                    if (resolver != null) {
                        return respondView(causeException, "bundle://adm/view/error/throwable.peb")

                    }

                    return respondFallback(causeException)
                }
            }
        } catch (internalException: Throwable) {
            LOG.error("Internal error occurred while rendering request error view", internalException)
            return respondFallback(causeException)
        }
    }

    private fun respondView(e: Throwable, uri: String): Response {
        val html = resolver!!.findOrFail(uri)
                .adaptTo(View::class)
                .with("message", ExceptionUtils.getRootCauseMessage(e))
                .with("stackTrace", ExceptionUtils.getRootCauseStackTrace(e).joinToString("\n"))
                .render()

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(html)
                .type(MediaType.TEXT_HTML)
                .build()
    }

    private fun respondFallback(e: Throwable): Response {
        val text = """${ExceptionUtils.getRootCauseMessage(e)}
${ExceptionUtils.getRootCauseStackTrace(e).joinToString("\n")}"""

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(text)
                .type(MediaType.TEXT_PLAIN)
                .build()
    }
}