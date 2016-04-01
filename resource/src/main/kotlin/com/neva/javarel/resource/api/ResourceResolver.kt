package com.neva.javarel.resource.api

import com.neva.javarel.foundation.api.adapting.Adapting

interface ResourceResolver : Adapting<Resource> {

    fun find(uri: String): Resource?

    fun findOrFail(uri: String): Resource
}
