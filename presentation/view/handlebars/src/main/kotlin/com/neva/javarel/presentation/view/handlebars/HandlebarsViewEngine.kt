package com.neva.javarel.presentation.view.handlebars

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.TemplateLoader
import com.google.common.collect.Sets
import com.neva.javarel.foundation.api.JavarelConstants
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.presentation.view.api.ViewEngine
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.*

@Component(
        immediate = true,
        metatype = true,
        label = "${JavarelConstants.SERVICE_PREFIX} Handlebars View Engine",
        description = "Logic-less and semantic Mustache templates with Java"
)
@Service(ViewEngine::class)
class HandlebarsViewEngine : ViewEngine {

    companion object {
        @Property(
                name = EXTENSION_PROP,
                value = ".hbs",
                label = "Extension",
                description = "Resource extension that engine will handle"
        )
        const val EXTENSION_PROP = "extension"
    }

    @Reference
    private lateinit var resourceResolver: ResourceResolver

    @Reference(
            referenceInterface = HandlebarsExtension::class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private var coreExtensions = Sets.newConcurrentHashSet<HandlebarsExtension>()

    private lateinit var props: Map<String, Any>

    val loader: TemplateLoader
        get() {
            val result = HandlebarsLoader(resourceResolver)
            result.suffix = extension

            return result
        }

    private var coreCached: Handlebars? = null

    val core: Handlebars
        @Synchronized
        get() {
            if (coreCached == null) {
                val handlebars = Handlebars(loader)
                coreExtensions.forEach { it.extend(handlebars) }

                coreCached = handlebars
            }
            return coreCached!!
        }

    @Suppress("UNCHECKED_CAST")
    val extension: String by lazy {
        props[EXTENSION_PROP] as String
    }

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return descriptor.path.endsWith(extension)
    }

    override fun make(resource: Resource): View {
        return HandlebarsView(this, resource)
    }

    private fun bindCoreExtensions(extension: HandlebarsExtension) {
        coreExtensions.add(extension)
        coreCached = null
    }

    private fun unbindCoreExtensions(extension: HandlebarsExtension) {
        coreExtensions.remove(extension)
        coreCached = null
    }

}