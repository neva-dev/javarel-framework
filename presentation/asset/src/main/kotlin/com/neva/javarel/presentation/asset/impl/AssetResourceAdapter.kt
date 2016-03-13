package com.neva.javarel.presentation.asset.impl

import com.google.common.collect.Sets
import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.presentation.asset.api.AssetFactory
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdapter
import org.apache.felix.scr.annotations.*
import kotlin.reflect.KClass

@Component
@Service
class AssetResourceAdapter : ResourceAdapter<Asset> {

    @Reference(referenceInterface = AssetFactory::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private val factories = Sets.newConcurrentHashSet<AssetFactory>()

    override val type: KClass<Asset>
        get() = Asset::class

    override fun adapt(resource: Resource): Asset {
        for (factory in factories) {
            if (factory.supports(resource.descriptor)) {
                return factory.make(resource)
            }
        }

        return FileAsset(resource)
    }

    private fun bindFactories(factory: AssetFactory) {
        factories.add(factory)
    }

    private fun unbindFactories(factory: AssetFactory) {
        factories.remove(factory)
    }

}
