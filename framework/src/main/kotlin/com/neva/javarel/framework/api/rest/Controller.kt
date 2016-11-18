package com.neva.javarel.framework.api.rest

import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.foundation.api.injection.Osgi
import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceResolver
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.storage.database.api.DatabaseAdmin
import com.neva.javarel.storage.store.api.StoreAdmin
import javax.inject.Inject

abstract class Controller {

    @Inject
    protected lateinit var guard: Guard

    @Osgi
    protected lateinit var dbAdmin: DatabaseAdmin

    @Osgi
    protected lateinit var storeAdmin: StoreAdmin

    @Osgi
    protected lateinit var urlGenerator: UrlGenerator

    @Osgi
    protected lateinit var resourceResolver: ResourceResolver

    protected fun view(resourceUri: String): View {
        return resourceResolver.findOrFail(resourceUri).adaptTo(View::class).with(context)
    }

    protected fun asset(path: String): Asset {
        return resourceResolver.findOrFail(path).adaptTo(Asset::class)
    }

    protected val context: Map<String, Any> by lazy {
        mapOf(
                "guard" to guard
        )
    }

}