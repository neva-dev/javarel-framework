package com.neva.javarel.presentation.view.handlebars

import com.github.jknack.handlebars.io.AbstractTemplateLoader
import com.github.jknack.handlebars.io.StringTemplateSource
import com.github.jknack.handlebars.io.TemplateSource
import com.neva.javarel.resource.api.ResourceResolver

class HandlebarsLoader(val resourceResolver: ResourceResolver) : AbstractTemplateLoader() {

    override fun sourceAt(location: String): TemplateSource {
        val resource = resourceResolver.findOrFail(location)
        val content = resource.input.bufferedReader().use { it.readText() }
        val fileName = resource.descriptor.baseName

        return StringTemplateSource(fileName, content)
    }

}