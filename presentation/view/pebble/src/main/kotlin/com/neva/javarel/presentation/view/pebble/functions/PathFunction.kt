package com.neva.javarel.presentation.view.pebble.functions

import com.mitchellbosecke.pebble.extension.Function

class PathFunction : Function {

    override fun execute(args: Map<String, Any>): Any {
        val path = args["0"].toString()

        // TODO URL rewriting here

        return path
    }

    override fun getArgumentNames(): MutableList<String>? {
        return null
    }

}
