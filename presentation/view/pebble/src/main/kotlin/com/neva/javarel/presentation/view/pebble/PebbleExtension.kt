package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.PebbleEngine

interface PebbleExtension {

    fun extend(engineBuilder: PebbleEngine.Builder)

}
