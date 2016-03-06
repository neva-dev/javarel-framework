package com.neva.javarel.presentation.asset.impl

import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.presentation.asset.api.AssetFactory
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdapter
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import kotlin.reflect.KClass

@Component(immediate = true)
@Instantiate
@Provides
class AssetResourceAdapter : ResourceAdapter<Asset> {

    @Requires(specification = AssetFactory::class)
    lateinit var factories: Set<AssetFactory>

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
}
