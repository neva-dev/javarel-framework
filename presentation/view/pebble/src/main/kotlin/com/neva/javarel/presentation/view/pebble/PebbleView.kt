package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.error.PebbleException
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.presentation.view.api.ViewException
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdaptee
import java.io.IOException
import java.io.StringWriter
import java.util.*

class PebbleView(val engine: PebbleEngine, resource: Resource) : ResourceAdaptee(resource), View {

    var context = HashMap<String, Any>()

    override fun render(): String {
        try {
            val uri = resource.descriptor.uri
            val template = engine.core.getTemplate(uri)
            val writer = StringWriter()

            template.evaluate(writer, context)
            return writer.toString()
        } catch (e: PebbleException) {
            throw ViewException("Cannot render view for a resource: '${resource.descriptor}'")
        } catch (e: IOException) {
            throw ViewException("Cannot render view for a resource: '${resource.descriptor}'")
        }
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
