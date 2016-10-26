package com.neva.javarel.presentation.view.handlebars

import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdaptee
import java.util.*

class HandlebarsView(val engine: HandlebarsViewEngine, resource: Resource) : ResourceAdaptee(resource), View {

    var context = HashMap<String, Any>()

    override fun render(): String {
        val source = engine.core.loader.sourceAt(resource.descriptor.uri)
        val template = engine.core.compile(source)

        return template.apply(context)
    }

    override fun with(key: String, value: Any): View {
        context.put(key, value)
        return this
    }

    override fun with(data: Map<String, Any>): View {
        context.putAll(data)
        return this
    }

}