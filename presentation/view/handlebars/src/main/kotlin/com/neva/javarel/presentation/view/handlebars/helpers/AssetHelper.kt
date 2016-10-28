package com.neva.javarel.presentation.view.handlebars.helpers

import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.Options
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.resource.api.ResourceMapper

class AssetHelper(val urlGenerator: UrlGenerator) : Helper<String> {

    companion object {
        val ROUTE_NAME = "asset"
        val PATH_PARAM = "path"
    }

    @Suppress("UNCHECKED_CAST")
    override fun apply(uri: String, options: Options): Any {
        val params = mutableMapOf<String, Any>()

        params.putAll(options.hash)
        params[PATH_PARAM] = ResourceMapper.uriToPath(uri)

        return urlGenerator.name(ROUTE_NAME, params)
    }
}