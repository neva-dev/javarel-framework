package com.neva.javarel.presentation.asset.impl.css

import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.presentation.asset.api.AssetFactory
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service

@Component
@Service
class CssAssetFactory : AssetFactory {

    override fun supports(descriptor: ResourceDescriptor): Boolean {
        return descriptor.path.endsWith(".css")
    }

    override fun make(resource: Resource): Asset {
        return CssAsset(resource)
    }
}
