package com.neva.javarel.presentation.view.pebble.functions

import com.mitchellbosecke.pebble.extension.Function
import java.util.*

class NowFunction : Function {

    override fun getArgumentNames(): MutableList<String>? {
        return null
    }

    override fun execute(args: Map<String, Any>): Any {
        return Date()
    }

}
