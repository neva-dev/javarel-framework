package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Extension
import com.mitchellbosecke.pebble.extension.Function
import com.neva.javarel.presentation.view.pebble.functions.AssetFunction
import com.neva.javarel.presentation.view.pebble.functions.NowFunction
import com.neva.javarel.presentation.view.pebble.functions.PathFunction
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides

@Component
@Instantiate
@Provides(specifications = arrayOf(Extension::class))
class PebbleExtension : AbstractExtension() {

    override fun getFunctions(): Map<String, Function> {
        return mapOf(
                "now" to NowFunction(),
                "path" to PathFunction(),
                "asset" to AssetFunction()
        )
    }
}
