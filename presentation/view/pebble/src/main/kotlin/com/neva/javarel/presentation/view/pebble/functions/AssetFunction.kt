package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.api.ViewException
import com.neva.javarel.resource.api.ResourceMapper

class AssetFunction(val urlGenerator: UrlGenerator) : BaseFunction() {

    companion object {
        val routeName = "asset"
        val pathParam = "path"
        val paramsParam = "params"
    }

    override fun getArgumentNames(): MutableList<String>? {
        return mutableListOf(pathParam, paramsParam)
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        val path = if (args.containsKey(pathParam)) {
            args.get(pathParam) as String
        } else if (args.size == 1) {
            args.entries.first().value as String
        } else {
            throw ViewException("Asset function should have 'path' argument specified.")
        }

        val params = FunctionUtils.copyParams(paramsParam, args)
        params.put(pathParam, ResourceMapper.uriToPath(path))

        return urlGenerator.name(routeName, params)
    }

}
