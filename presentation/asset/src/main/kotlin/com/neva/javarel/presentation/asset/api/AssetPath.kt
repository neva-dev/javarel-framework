package com.neva.javarel.presentation.asset.api

import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.resource.api.ResourceMapper

class AssetPath(private val urlGenerator: UrlGenerator) {

    companion object {
        const val ROUTE_NAME = "asset"
        const val PATH_PARAM = "path"
    }

    fun generate(uri: String, options: Map<String, Any> = mapOf()): String {
        val params = mutableMapOf<String, Any>()

        params.putAll(options)
        params[PATH_PARAM] = ResourceMapper.uriToPath(uri)

        return urlGenerator.name(ROUTE_NAME, params)
    }

}