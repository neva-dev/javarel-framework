package com.neva.javarel.presentation.view.handlebars

import com.github.jknack.handlebars.Handlebars
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.handlebars.helpers.RouteHelper
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

@Component(immediate = true)
@Service(HandlebarsExtension::class)
class DefaultHandlebarsExtension : HandlebarsExtension {

    @Reference
    private lateinit var urlGenerator: UrlGenerator

    override fun extend(engine: Handlebars) {
        engine.registerHelper("route", RouteHelper(urlGenerator))
    }

}