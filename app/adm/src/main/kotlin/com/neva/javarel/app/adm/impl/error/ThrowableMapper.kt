package com.neva.javarel.app.adm.impl.error

import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceNotFoundException
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.ReferenceCardinality
import org.apache.felix.scr.annotations.Service
import org.slf4j.LoggerFactory
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Component
@Service(RestComponent::class)
@Provider
class ThrowableMapper : ExceptionMapper<Throwable>, RestComponent {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    private var resolver: ResourceResolver? = null

    companion object {
        val logger = LoggerFactory.getLogger(ThrowableMapper::class.java)
    }

    override fun toResponse(causeException: Throwable): Response {
        try {
            when (causeException) {
                is NotFoundException,
                is ResourceNotFoundException -> {
                    logger.debug("Resource not found", causeException)

                    return respondView(causeException, "bundle://adm/view/error/not-found.peb")
                }
                else -> {
                    logger.error("Request error", causeException)
                    if (resolver != null) {
                        return respondView(causeException, "bundle://adm/view/error/throwable.peb")

                    }

                    return respondFallback(causeException)
                }
            }
        } catch (internalException: Throwable) {
            logger.error("Internal error occurred while rendering request error view", internalException)
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