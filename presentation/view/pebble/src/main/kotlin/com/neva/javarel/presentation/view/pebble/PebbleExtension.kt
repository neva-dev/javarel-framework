package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Extension
import com.mitchellbosecke.pebble.extension.Function
import com.neva.javarel.communication.rest.api.RestRouter
import com.neva.javarel.presentation.view.pebble.functions.AssetFunction
import com.neva.javarel.presentation.view.pebble.functions.NowFunction
import com.neva.javarel.presentation.view.pebble.functions.RouteFunction
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires

@Component(immediate = true)
@Instantiate
@Provides(specifications = arrayOf(Extension::class))
class PebbleExtension : AbstractExtension() {

    @Requires
    private lateinit var router : RestRouter

    override fun getFunctions(): Map<String, Function> {
        return mapOf(
                "now" to NowFunction(),
                "route" to RouteFunction(router),
                "asset" to AssetFunction(router)
        )
    }
}
