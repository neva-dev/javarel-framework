package com.neva.javarel.resource.api

interface ResourceProvider {

    fun handles(descriptor: ResourceDescriptor): Boolean

    fun provide(resolver: ResourceResolver, descriptor: ResourceDescriptor): Resource?
}
