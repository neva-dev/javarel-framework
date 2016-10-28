package com.neva.javarel.presentation.view.pebble.functions

import java.util.*

class NowFunction : BaseFunction() {

    override fun execute(args: Map<String, Any>): Any {
        return Date()
    }

}
