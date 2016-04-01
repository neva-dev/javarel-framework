package com.neva.javarel.resource.api

import com.neva.javarel.foundation.adapting.AdapterFactory

interface ResourceResolver : AdapterFactory<Resource> {

    fun find(uri: String): Resource?

    fun findOrFail(uri: String): Resource
}
