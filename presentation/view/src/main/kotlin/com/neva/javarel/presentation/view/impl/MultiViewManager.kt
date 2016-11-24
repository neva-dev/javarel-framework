package com.neva.javarel.presentation.view.impl

import com.google.common.collect.Sets
import com.neva.javarel.foundation.api.adapting.Adapter
import com.neva.javarel.presentation.view.api.View
import com.neva.javarel.presentation.view.api.ViewEngine
import com.neva.javarel.presentation.view.api.ViewManager
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdapter
import com.neva.javarel.resource.api.ResourceException
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.*
import kotlin.reflect.KClass

@Component(immediate = true)
@Service(ViewManager::class, Adapter::class)
class MultiViewManager : ViewManager, ResourceAdapter<View>() {

    @Reference(
            referenceInterface = ViewEngine::class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    private val engines = Sets.newConcurrentHashSet<ViewEngine>()

    @Reference(cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    private lateinit var resourceResolver: ResourceResolver

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

    override fun make(resource: Resource): View {
        return adapt(resource)
    }

    override fun make(resourceUri: String): View {
        return make(resourceResolver.findOrFail(resourceUri))
    }

    override fun make(template: String, extension: String): View {
        return make(ViewTemplateResource(template, extension, resourceResolver))
    }

    protected fun bindViewEngine(engine: ViewEngine) {
        engines.add(engine)
    }

    protected fun unbindViewEngine(engine: ViewEngine) {
        engines.remove(engine)
    }
}