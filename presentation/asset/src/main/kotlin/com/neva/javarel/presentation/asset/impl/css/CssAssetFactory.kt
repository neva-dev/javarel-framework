package com.neva.javarel.presentation.asset.impl.css

import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.presentation.asset.api.AssetFactory
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides

@Component
@Instantiate
@Provides
class CssAssetFactory : AssetFactory {

    override fun supports(descriptor: ResourceDescriptor): Boolean {
        return descriptor.path.endsWith(".css")
    }

    override fun make(resource: Resource): Asset {
        return CssAsset(resource)
    }
}
