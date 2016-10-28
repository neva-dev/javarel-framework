package com.neva.javarel.presentation.view.handlebars.helpers

import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.Options
import com.neva.javarel.communication.rest.api.UrlGenerator
import com.neva.javarel.presentation.view.api.ViewException

/**
 * Generate an URL using route (by action or name) and its parameters
 */
class RouteHelper(val urlGenerator: UrlGenerator) : Helper<String> {

    override fun apply(pair: String, options: Options): Any {
        val parts = pair.split(":")
        val (type, arg) = when (parts.size) {
            1 -> Pair("action", parts[0])
            2 -> Pair(parts[0], parts[1])
            else -> throw ViewException("Route helper expects first argument to be in format [type:value] e.g 'name:home'")
        }

        return when (type) {
            "action" -> {
                urlGenerator.action(arg, options.hash)
            }
            "name" -> {
                urlGenerator.name(arg, options.hash)
            }
            else -> urlGenerator.action(arg, options.hash)
        }
    }


}