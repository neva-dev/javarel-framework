package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.communication.rest.api.UrlGenerator

class RouteFunction(val urlGenerator: UrlGenerator) : BaseFunction() {

    companion object {
        val actionParam = "action"
        val nameParam = "name"
        val paramsParam = "params"
    }

    override fun getArgumentNames(): MutableList<String>? {
        return mutableListOf(actionParam, nameParam, paramsParam)
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        if (args.isEmpty()) {
            throw IllegalArgumentException("Route function requires 'action' or 'name' argument specified.")
        }

        val params = FunctionUtils.copyParams(paramsParam, args)

        if (args.containsKey(nameParam)) {
            return urlGenerator.name(args.get(nameParam) as String, params)
        } else if (args.containsKey(actionParam)) {
            return urlGenerator.action(args.get(actionParam) as String, params)
        }

        return urlGenerator.action(args.entries.first().value as String, params)
    }


}
