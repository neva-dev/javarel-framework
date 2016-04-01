package com.neva.javarel.presentation.view.impl

import com.google.common.collect.Sets
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.presentation.view.api.ViewEngine
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdapter
import com.neva.javarel.resource.api.ResourceException
import org.apache.felix.scr.annotations.*
import kotlin.reflect.KClass

@Component
@Service
class ViewResourceAdapter : ResourceAdapter<View>() {

    @Reference(referenceInterface = ViewEngine::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private val engines = Sets.newConcurrentHashSet<ViewEngine>()

    override val targetType: KClass<View>
        get() = View::class

    override fun adapt(adaptable: Resource): View {
        for (engine in engines) {
            if (engine.handles(adaptable.descriptor)) {
                return engine.make(adaptable)
            }
        }

        throw ResourceException("Cannot find a view engine for resource: '${adaptable.descriptor}'")
    }

    protected fun bindViewEngine(engine: ViewEngine) {
        engines.add(engine)
    }

    protected fun unbindViewEngine(engine: ViewEngine) {
        engines.remove(engine)
    }
}