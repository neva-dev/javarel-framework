package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceResolver

/**
 * Allows to mix view engines
 */
class RenderFunction(val resourceResolver: ResourceResolver) : BaseFunction() {

    companion object {
        val URI_PARAM = "uri"
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        val params = mutableMapOf<String, Any>()
        params.putAll(args)

        val uri = (params.remove(URI_PARAM) ?: params.values.first()) as String

        return resourceResolver.findOrFail(uri)
                .adaptTo(View::class)
                .with(params)
                .render()
    }

}
