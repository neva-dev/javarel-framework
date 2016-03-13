package com.neva.javarel.presentation.view.impl

import com.google.common.collect.Sets
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.presentation.view.api.ViewEngine
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdapter
import com.neva.javarel.resource.api.ResourceException
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.ReferenceCardinality
import org.apache.felix.scr.annotations.Service
import kotlin.reflect.KClass

@Component
@Service
class ViewResourceAdapter : ResourceAdapter<View> {

    @Reference(referenceInterface = ViewEngine::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
    private val engines = Sets.newConcurrentHashSet<ViewEngine>()

    override val type: KClass<View>
        get() = View::class

    override fun adapt(resource: Resource): View {
        for (engine in engines) {
            if (engine.handles(resource.descriptor)) {
                return engine.make(resource)
            }
        }

        throw ResourceException("Cannot find a view engine for resource: '${resource.descriptor}'")
    }

    protected fun bindViewEngine(engine: ViewEngine) {
        engines.add(engine)
    }

    protected fun unbindViewEngine(engine: ViewEngine) {
        engines.remove(engine)
    }
}