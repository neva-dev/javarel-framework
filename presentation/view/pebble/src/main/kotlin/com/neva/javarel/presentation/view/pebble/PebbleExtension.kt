package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Extension
import com.mitchellbosecke.pebble.extension.Function
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.pebble.functions.AssetFunction
import com.neva.javarel.presentation.view.pebble.functions.NowFunction
import com.neva.javarel.presentation.view.pebble.functions.RouteFunction
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

@Component(immediate = true)
@Service(Extension::class)
class PebbleExtension : AbstractExtension() {

    @Reference
    private lateinit var urlGenerator : UrlGenerator

    override fun getFunctions(): Map<String, Function> {
        return mapOf(
                "now" to NowFunction(),
                "route" to RouteFunction(urlGenerator),
                "asset" to AssetFunction(urlGenerator)
        )
    }
}
