package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.communication.rest.api.RestUrlGenerator
import com.neva.javarel.presentation.view.api.ViewException
import com.neva.javarel.resource.api.ResourceMapper

class AssetFunction(val urlGenerator: RestUrlGenerator) : BaseFunction() {

    companion object {
        val routeName = "asset"
        val pathParam = "path"
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        val params = getParams(args)

        val path = if (params.containsKey(pathParam)) {
            params.get(pathParam) as String
        } else if (params.size == 1) {
            params.entries.first().value as String
        } else {
            throw ViewException("Asset function should have 'path' argument specified.")
        }

        params.put(pathParam, ResourceMapper.uriToPath(path))

        return urlGenerator.name(routeName, params)
    }

    override fun getArgumentNames(): MutableList<String>? {
        return mutableListOf(routeName)
    }

}
