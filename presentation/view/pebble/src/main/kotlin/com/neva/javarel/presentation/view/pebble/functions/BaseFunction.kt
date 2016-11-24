package com.neva.javarel.presentation.view.pebble.functions

import com.mitchellbosecke.pebble.extension.Function
import com.neva.javarel.presentation.view.api.ViewException

abstract class BaseFunction : Function {

    override fun getArgumentNames(): MutableList<String> {
        return mutableListOf()
    }

    protected fun firstArgument(args: Map<String, Any>): Any {
        return args.entries.firstOrNull() ?: throw ViewException("Pebble function ${javaClass.canonicalName} expects at least one argument.")
    }

}