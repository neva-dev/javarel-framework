package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.api.ViewException
import com.neva.javarel.resource.api.ResourceMapper

class AssetFunction(val urlGenerator: UrlGenerator) : BaseFunction() {

    companion object {
        val ROUTE_NAME = "asset"
        val PATH_PARAM = "path"
        val PARAMS_PARAM = "params"
    }

    override fun getArgumentNames(): MutableList<String>? {
        return mutableListOf(PATH_PARAM, PARAMS_PARAM)
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        val path = if (args.containsKey(PATH_PARAM)) {
            args.get(PATH_PARAM) as String
        } else if (args.size == 1) {
            args.entries.first().value as String
        } else {
            throw ViewException("Asset function should have 'path' argument specified.")
        }

        val params = FunctionUtils.copyParams(PARAMS_PARAM, args)
        params.put(PATH_PARAM, ResourceMapper.uriToPath(path))

        return urlGenerator.name(ROUTE_NAME, params)
    }

}
