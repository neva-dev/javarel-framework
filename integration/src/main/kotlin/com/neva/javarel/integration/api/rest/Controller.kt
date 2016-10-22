package com.neva.javarel.integration.api.rest

import com.neva.javarel.communication.rest.api.RestRequest
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceResolver
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.storage.api.DatabaseAdmin
import javax.ws.rs.core.Context

abstract class Controller {

    @Context
    protected lateinit var request: RestRequest

    @Uses
    protected lateinit var guard: Guard

    @Uses
    protected lateinit var db: DatabaseAdmin

    @Uses
    protected lateinit var urlGenerator: UrlGenerator

    @Uses
    protected lateinit var resourceResolver: ResourceResolver

    protected fun view(resourceUri: String): View {
        return resourceResolver.findOrFail(resourceUri).adaptTo(View::class).with(viewGlobals())
    }

    protected fun viewGlobals(): Map<String, Any> {
        return mapOf(
                "request" to request,
                "guard" to guard
        )
    }

}