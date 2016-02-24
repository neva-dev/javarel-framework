package com.neva.javarel.storage.api

interface ResourceProvider {

    fun handles(descriptor: ResourceDescriptor): Boolean

    fun provide(resolver: ResourceResolver, descriptor: ResourceDescriptor): Resource?
}
