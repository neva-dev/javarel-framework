package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.asset.api.AssetPath
import com.neva.javarel.presentation.view.api.ViewException

class AssetFunction(val urlGenerator: UrlGenerator) : BaseFunction() {

    companion object {
        val URI_PARAM = "uri"
        val PARAMS_PARAM = "params"
    }

    override fun getArgumentNames(): MutableList<String> {
        return mutableListOf(URI_PARAM, PARAMS_PARAM)
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        val uri = if (args.containsKey(URI_PARAM)) {
            args.get(URI_PARAM) as String
        } else if (args.size == 1) {
            args.entries.first().value as String
        } else {
            throw ViewException("Asset function should have 'path' argument specified.")
        }

        val params = FunctionUtils.copyParams(PARAMS_PARAM, args)

        return AssetPath(urlGenerator).generate(uri, params)
    }

}
