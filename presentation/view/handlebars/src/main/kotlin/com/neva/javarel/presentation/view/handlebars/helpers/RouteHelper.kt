package com.neva.javarel.presentation.view.handlebars.helpers

import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.Options
import com.neva.javarel.communication.rest.api.UrlGenerator

class RouteHelper(val urlGenerator: UrlGenerator) : Helper<String> {

    @Suppress("UNCHECKED_CAST")
    override fun apply(type: String, options: Options): Any {
        val allParams = options.params.toList() as List<String>
        val arg = if (allParams.isNotEmpty()) allParams.first() else type
        val params = if (allParams.size > 1) allParams.subList(1, allParams.size) else listOf()

        return when (type) {
            "action" -> {
                urlGenerator.action(arg, params)
            }
            "name" -> {
                urlGenerator.name(arg, params)
            }
            else -> urlGenerator.action(arg, params)
        }
    }


}