package com.neva.javarel.presentation.asset.impl

import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdapter
import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.presentation.asset.api.AssetFactory
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires

@Component(immediate = true)
@Instantiate
@Provides
class AssetResourceAdapter : ResourceAdapter {

    @Requires(specification = AssetFactory::class)
    lateinit var factories: Set<AssetFactory>

    override val type: Class<Any>
        get() = Asset::class as Class<Any>

    override fun adapt(resource: Resource): Any {
        for (factory in factories) {
            if (factory.supports(resource.descriptor)) {
                return factory.make(resource)
            }
        }

        return FileAsset(resource)
    }
}
