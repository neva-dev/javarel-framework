package com.neva.javarel.presentation.asset.api

import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor

interface AssetFactory {

    fun supports(descriptor: ResourceDescriptor): Boolean

    fun make(resource: Resource): Asset

}
