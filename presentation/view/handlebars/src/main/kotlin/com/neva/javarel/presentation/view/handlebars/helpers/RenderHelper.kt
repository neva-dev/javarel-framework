package com.neva.javarel.presentation.view.handlebars.helpers

import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.Options
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.ResourceResolver

/**
 * Allows to mix view engines
 */
class RenderHelper(val resourceResolver: ResourceResolver) : Helper<String> {

    override fun apply(uri: String, options: Options): Any {
        return resourceResolver.findOrFail(uri)
                .adaptTo(View::class)
                .with(options.hash)
                .render()
    }
}