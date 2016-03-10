package com.neva.javarel.presentation.view.pebble.functions

import com.mitchellbosecke.pebble.extension.Function
import com.neva.javarel.communication.rest.api.RestRouter

class RouteFunction(val router: RestRouter) : Function {

    companion object {
        val actionParam = "action"

        val nameParam = "name"
    }

    override fun getArgumentNames(): MutableList<String>? {
        return mutableListOf(actionParam, nameParam)
    }

    override fun execute(args: MutableMap<String, Any>): Any {
        if (args.isEmpty()) {
            throw IllegalArgumentException("Route function requires 'action' or 'name' argument specified.")
        }

        if (args.containsKey(nameParam)) {
            val name = args.get(nameParam) as String
            args.remove(nameParam);

            return byName(name, args)
        } else if (args.containsKey(actionParam)) {
            val action = args.get(actionParam) as String;
            args.remove(actionParam);

            return byAction(action)
        }

        return byAction(args.entries.first().value as String)
    }

    private fun byAction(action: String, params: Map<String, Any> = emptyMap()): String {
        return router.routeByAction(action).assembleUri(params)
    }

    private fun byName(name: String, params: Map<String, Any> = emptyMap()): String {
        return router.routeByName(name).assembleUri(params)
    }

}
