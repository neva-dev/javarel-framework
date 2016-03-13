package com.neva.javarel.presentation.asset.impl.js

import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.presentation.asset.api.AssetFactory
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service

@Component
@Service
class JsAssetFactory : AssetFactory {

    override fun supports(descriptor: ResourceDescriptor): Boolean {
        return descriptor.path.endsWith(".js")
    }

    override fun make(resource: Resource): Asset {
        return JsAsset(resource)
    }
}
