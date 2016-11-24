package com.neva.javarel.presentation.view.pebble

import com.google.common.collect.Sets
import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.extension.Extension
import com.mitchellbosecke.pebble.loader.Loader
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
        label = "${JavarelConstants.SERVICE_PREFIX} Pebble View Engine",
        description = "Twig inspired, fastest robust template engine in Java"
)
@Service(ViewEngine::class)
class PebbleViewEngine : ViewEngine {

    companion object {
        @Property(
                name = EXTENSIONS_PROP,
                value = ".peb",
                label = "Extensions",
                description = "Resource extensions that engine will handle",
                unbounded = PropertyUnbounded.ARRAY
        )
        const val EXTENSIONS_PROP = "extensions"

        @Property(
                name = STRICT_PROP,
                boolValue = booleanArrayOf(STRICT_DEFAULT),
                label = "Strict",
                description = "Verbose mode in which any undefined variable will cause an exception. Recommended only for development purposes."
        )
        const val STRICT_PROP = "strict"
        const val STRICT_DEFAULT = true
    }

    private lateinit var props: Map<String, Any>

    @Reference(cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    private lateinit var resourceResolver: ResourceResolver

    private var coreCached: PebbleEngine? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    @Reference(
            referenceInterface = PebbleExtension::class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private var coreExtensions = Sets.newConcurrentHashSet<PebbleExtension>()

    @Suppress("UNCHECKED_CAST")
    val extensions: Array<String> by lazy {
        props[EXTENSIONS_PROP] as Array<String>? ?: arrayOf()
    }

    val strict: Boolean by lazy {
        props[STRICT_PROP] as Boolean? ?: STRICT_DEFAULT
    }

    val loader: Loader<String>
        get() {
            return PebbleLoader(resourceResolver)
        }

    val core: PebbleEngine
        @Synchronized
        get() {
            if (coreCached == null) {
                val builder = PebbleEngine.Builder()
                        .strictVariables(strict)
                        .loader(loader)

                coreExtensions.forEach { it.extend(builder) }
                coreCached = builder.build()
            }
            return coreCached!!
        }

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return extensions.toList().any { descriptor.path.endsWith(it) }
    }

    override fun make(resource: Resource): View {
        return PebbleView(this, resource)
    }

    private fun bindCoreExtensions(extension: PebbleExtension) {
        coreExtensions.add(extension)
        coreCached = null
    }

    private fun unbindCoreExtensions(extension: PebbleExtension) {
        coreExtensions.remove(extension)
        coreCached = null
    }
}
