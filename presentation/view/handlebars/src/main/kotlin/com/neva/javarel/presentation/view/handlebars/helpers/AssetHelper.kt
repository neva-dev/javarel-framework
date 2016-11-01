package com.neva.javarel.presentation.view.handlebars.helpers

import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.Options
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.asset.api.AssetPath

class AssetHelper(val urlGenerator: UrlGenerator) : Helper<String> {

    override fun apply(uri: String, options: Options): Any {
        return AssetPath(urlGenerator).generate(uri, options.hash)
    }

}