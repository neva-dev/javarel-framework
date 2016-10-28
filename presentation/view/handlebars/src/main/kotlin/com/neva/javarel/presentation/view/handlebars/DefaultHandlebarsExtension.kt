package com.neva.javarel.presentation.view.handlebars

import com.github.jknack.handlebars.Handlebars
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.handlebars.helpers.AssetHelper
import com.neva.javarel.presentation.view.handlebars.helpers.RenderHelper
import com.neva.javarel.presentation.view.handlebars.helpers.RouteHelper
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

@Component(immediate = true)
@Service(HandlebarsExtension::class)
class DefaultHandlebarsExtension : HandlebarsExtension {

    @Reference
    private lateinit var urlGenerator: UrlGenerator

    @Reference
    private lateinit var resourceResolver: ResourceResolver

    override fun extend(engine: Handlebars) {
        engine.registerHelper("asset", AssetHelper(urlGenerator))
        engine.registerHelper("render", RenderHelper(resourceResolver))
        engine.registerHelper("route", RouteHelper(urlGenerator))
    }

}