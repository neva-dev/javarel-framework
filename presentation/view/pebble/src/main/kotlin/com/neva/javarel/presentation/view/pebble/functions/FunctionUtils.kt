package com.neva.javarel.presentation.view.pebble.functions

object FunctionUtils {

    @Suppress("UNCHECKED_CAST")
    fun copyParams(argName: String, args: MutableMap<String, Any>): MutableMap<String, Any> {
        val params = mutableMapOf<String, Any>()
        if (args.containsKey(argName)) {
            params.putAll(args.get(argName) as Map<String, Any>)
        }

        return params
    }

}