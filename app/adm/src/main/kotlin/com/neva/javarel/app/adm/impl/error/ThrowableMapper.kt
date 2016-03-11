package com.neva.javarel.app.adm.impl.error

import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceNotFoundException
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import org.slf4j.LoggerFactory
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Component
@Instantiate
@Provides(specifications = arrayOf(RestComponent::class))
@Provider
class ThrowableMapper : ExceptionMapper<Throwable>, RestComponent {

    @Requires(optional = true)
    private var resolver: ResourceResolver? = null

    companion object {
        val logger = LoggerFactory.getLogger(ThrowableMapper::class.java)
    }

    override fun toResponse(causeException: Throwable): Response {
        when (causeException) {
            is NotFoundException,
            is ResourceNotFoundException -> {
                logger.debug("Resource not found", causeException)

                return respondView(causeException, "bundle://adm/view/error/not-found.peb")
            }
            else -> {
                logger.error("Request error", causeException)

                if (resolver != null) {
                    try {
                        return respondView(causeException, "bundle://adm/view/error/throwable.peb")
                    } catch (internalException: Throwable) {
                        logger.error("Internal error occurred while rendering request error view", internalException)
                        return respondFallback(causeException)
                    }
                }

                return respondFallback(causeException)
            }
        }
    }

    private fun respondView(e: Throwable, uri: String): Response {
        val html = resolver!!.resolve(uri)
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