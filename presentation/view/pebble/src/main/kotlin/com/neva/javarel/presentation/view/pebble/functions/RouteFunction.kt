package com.neva.javarel.presentation.view.pebble.functions

import com.mitchellbosecke.pebble.extension.Function
import com.neva.javarel.communication.rest.api.RestRouter

class RouteFunction(val router: RestRouter) : Function {

    companion object {
        val actionParam = "action"

        val nameParam = "name"
    }

    override fun execute(args: Map<String, Any>): Any {
        if (args.isEmpty()) {
            throw IllegalArgumentException("Route function requires 'action' or 'name' argument specified.")
        }

        if (args.containsKey(actionParam)) {
            return byAction(args.get(actionParam) as String)
        }

        return byAction(args.entries.first().value as String)
    }

    private fun byAction(action: String): String {
        return router.routeByAction(action).uri
    }

    override fun getArgumentNames(): MutableList<String>? {
        return mutableListOf(actionParam, nameParam)
    }

}
