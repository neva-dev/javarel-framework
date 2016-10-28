package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Function
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.pebble.functions.AssetFunction
import com.neva.javarel.presentation.view.pebble.functions.NowFunction
import com.neva.javarel.presentation.view.pebble.functions.RenderFunction
import com.neva.javarel.presentation.view.pebble.functions.RouteFunction
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

@Component(immediate = true)
@Service(PebbleExtension::class)
class DefaultPebbleExtension : PebbleExtension {

    @Reference
    private lateinit var urlGenerator: UrlGenerator

    @Reference
    private lateinit var resourceResolver: ResourceResolver

    override fun extend(engineBuilder: PebbleEngine.Builder) {
        engineBuilder.extension(object : AbstractExtension() {
            override fun getFunctions(): MutableMap<String, Function> {
                return mutableMapOf(
                        "asset" to AssetFunction(urlGenerator),
                        "render" to RenderFunction(resourceResolver),
                        "route" to RouteFunction(urlGenerator),
                        "now" to NowFunction()
                )
            }
        })
    }

}