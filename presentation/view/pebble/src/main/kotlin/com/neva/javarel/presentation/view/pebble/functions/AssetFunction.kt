package com.neva.javarel.presentation.view.pebble.functions

import com.mitchellbosecke.pebble.extension.Function
import com.neva.javarel.resource.api.ResourceMapper

class AssetFunction : Function {

    override fun execute(args: Map<String, Any>): Any {
        val path = args["0"].toString()
        return "/asset/" + ResourceMapper.uriToPath(path)
    }

    override fun getArgumentNames(): MutableList<String>? {
        return null
    }


}
