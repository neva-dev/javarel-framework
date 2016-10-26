package com.neva.javarel.presentation.view.handlebars.helpers

import com.github.jknack.handlebars.Options
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.handlebars.HandlebarsHelper
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

@Component(immediate = true)
@Service(HandlebarsHelper::class)
class RouteHelper : HandlebarsHelper<String> {

    @Reference
    private lateinit var urlGenerator: UrlGenerator

    override val name: String
        get() = "route"

    override fun apply(context: String, options: Options): Any {
        return "javascript:alert('Route to be calculated using Handlebars helper');"
    }

}