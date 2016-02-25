package com.neva.javarel.presentation.view.impl

import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.presentation.view.api.ViewEngine
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdapter
import com.neva.javarel.resource.api.ResourceException
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires

@Component
@Provides
@Instantiate
class ViewResourceAdapter : ResourceAdapter {

    @Requires(specification = ViewEngine::class)
    lateinit var engines: List<ViewEngine>

    override val type: Class<Any>
        get() = View::class as Class<Any>

    override fun adapt(resource: Resource): Any {
        for (engine in engines) {
            if (engine.handles(resource.descriptor)) {
                return engine.make(resource)
            }
        }

        throw ResourceException("Cannot find a view engine for resource: '${resource.descriptor}'")
    }
}