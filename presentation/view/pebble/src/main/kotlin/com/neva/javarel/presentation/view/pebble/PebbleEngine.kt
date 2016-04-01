package com.neva.javarel.presentation.view.pebble

import com.google.common.collect.Sets
import com.mitchellbosecke.pebble.extension.Extension
import com.mitchellbosecke.pebble.loader.Loader
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.presentation.view.api.ViewEngine
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.*

@Component(immediate = true)
@Service
class PebbleEngine : ViewEngine {

    companion object {
        val extension = ".peb"
    }

    @Reference
    private lateinit var resourceResolver: ResourceResolver

    @Reference(referenceInterface = Extension::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var extensions = Sets.newConcurrentHashSet<Extension>()

    val loader: Loader<String>
        get() {
            return PebbleLoader(resourceResolver)
        }

    val core: com.mitchellbosecke.pebble.PebbleEngine
        /**
         * TODO Make Pebble engine configurable
         */
        get() {
            return com.mitchellbosecke.pebble.PebbleEngine.Builder()
                    .strictVariables(true)
                    .loader(loader)
                    .extension(*extensions.toTypedArray())
                    .build()
        }

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return descriptor.path.endsWith(extension)
    }

    override fun make(resource: Resource): View {
        return PebbleView(this, resource)
    }

    private fun bindExtensions(extension: Extension) {
        extensions.add(extension)
    }

    private fun unbindExtensions(extension: Extension) {
        extensions.remove(extension)
    }
}
