package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.communication.rest.api.RestUrlGenerator

class RouteFunction(val urlGenerator: RestUrlGenerator) : BaseFunction() {

    companion object {
        val actionParam = "action"
        val nameParam = "name"
    }

    override fun getArgumentNames(): MutableList<String>? {
        return mutableListOf(actionParam, nameParam)
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        val params = getParams(args)

        if (params.isEmpty()) {
            throw IllegalArgumentException("Route function requires 'action' or 'name' argument specified.")
        }

        if (params.containsKey(nameParam)) {
            val name = params.get(nameParam) as String
            params.remove(nameParam);

            return urlGenerator.name(name, params)
        } else if (params.containsKey(actionParam)) {
            val action = params.get(actionParam) as String;
            params.remove(actionParam);

            return urlGenerator.action(action)
        }

        return urlGenerator.action(params.entries.first().value as String)
    }

}
