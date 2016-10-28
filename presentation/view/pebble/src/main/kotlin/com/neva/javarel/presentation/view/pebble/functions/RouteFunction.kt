package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.api.ViewException

/**
 * Generate an URL using route (by action or name) and its parameters
 */
class RouteFunction(val urlGenerator: UrlGenerator) : BaseFunction() {

    companion object {
        val ACTION_PARAM = "action"
        val NAME_PARAM = "name"
        val PARAMS_PARAM = "params"
    }

    override fun getArgumentNames(): MutableList<String> {
        return mutableListOf(ACTION_PARAM, NAME_PARAM, PARAMS_PARAM)
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        if (args.isEmpty()) {
            throw ViewException("Route function requires 'action' or 'name' argument specified.")
        }

        val params = FunctionUtils.copyParams(PARAMS_PARAM, args)

        if (args.containsKey(NAME_PARAM)) {
            return urlGenerator.name(args.get(NAME_PARAM) as String, params)
        } else if (args.containsKey(ACTION_PARAM)) {
            return urlGenerator.action(args.get(ACTION_PARAM) as String, params)
        }

        return urlGenerator.action(args.entries.first().value as String, params)
    }


}
