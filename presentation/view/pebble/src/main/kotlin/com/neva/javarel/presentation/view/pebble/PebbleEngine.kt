package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.extension.Extension
import com.mitchellbosecke.pebble.loader.Loader
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.presentation.view.api.ViewEngine
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires

@Component
@Instantiate
@Provides
class PebbleEngine : ViewEngine {

    companion object {
        val extension = ".peb"
    }

    @Requires(specification = Extension::class)
    lateinit var extensions: Set<Extension>

    @Requires
    lateinit var resourceResolver: ResourceResolver

    val loader: Loader<String> by lazy {
        PebbleLoader(resourceResolver)
    }

    val core: com.mitchellbosecke.pebble.PebbleEngine
        get() {
            return com.mitchellbosecke.pebble.PebbleEngine.Builder()
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
}
