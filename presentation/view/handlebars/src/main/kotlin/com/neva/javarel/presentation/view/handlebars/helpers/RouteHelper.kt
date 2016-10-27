package com.neva.javarel.presentation.view.handlebars.helpers

import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.Options
import com.neva.javarel.communication.rest.api.UrlGenerator

class RouteHelper(val urlGenerator: UrlGenerator) : Helper<String> {

    override fun apply(context: String, options: Options): Any {
        return "javascript:alert('Route to be calculated using Handlebars helper');"
    }

}