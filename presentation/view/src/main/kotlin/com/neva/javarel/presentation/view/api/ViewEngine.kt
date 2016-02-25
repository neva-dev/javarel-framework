package com.neva.javarel.presentation.view.api

import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor

interface ViewEngine {

    fun handles(descriptor: ResourceDescriptor): Boolean

    fun make(resource: Resource): View
}
