package com.neva.javarel.presentation.view.pebble.functions

import com.mitchellbosecke.pebble.extension.Function

abstract class BaseFunction : Function {

    companion object {
        val contextParam = "_context"
        val selfParam = "_self"
    }

    protected fun getParams(args: MutableMap<String, Any>): MutableMap<String, Any> {
        args.remove(contextParam)
        args.remove(selfParam)

        return args
    }

}