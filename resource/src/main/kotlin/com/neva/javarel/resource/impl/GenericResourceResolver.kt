package com.neva.javarel.resource.impl

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.neva.javarel.foundation.api.adapting.Adaptee
import com.neva.javarel.foundation.api.adapting.AdaptingManager
import com.neva.javarel.resource.api.*
import org.apache.felix.scr.annotations.*
import kotlin.reflect.KClass

@Component
@Service
class GenericResourceResolver : ResourceResolver, Adaptee {

    @Reference
    private lateinit var adaptingManager: AdaptingManager

    @Reference(referenceInterface = ResourceProvider::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var providers = Sets.newConcurrentHashSet<ResourceProvider>()

    override fun find(uri: String): Resource? {
        val descriptor = ResourceDescriptor(uri)
        return provideResource(descriptor, findProviders(descriptor))
    }

    override fun findOrFail(uri: String): Resource {
        val descriptor = ResourceDescriptor(ResourceMapper.fixUri(uri))
        val providers = findProviders(descriptor)

        if (providers.isEmpty()) {
            throw ResourceNotFoundException("Cannot find any provider for resource: '${descriptor.uri}'")
        }

        val resource = provideResource(descriptor, providers)
        if (resource == null) {
            throw ResourceNotFoundException("Resource by URI cannot be found: '${descriptor.uri}'")
        }

        return resource
    }

    override fun <T : Any> adapt(adaptable: Resource, clazz: KClass<T>): T {
        return adaptingManager.adapt(adaptable, clazz)
    }

    override fun <T : Any> adaptTo(clazz: KClass<T>): T {
        return adaptingManager.adapt(this, clazz)
    }

    private fun provideResource(descriptor: ResourceDescriptor, providers: List<ResourceProvider>): Resource? {
        var resource: Resource? = null
        for (provider in providers) {
            resource = provider.provide(this, descriptor)
            if (resource != null) {
                break
            }
        }

        return resource
    }

    private fun findProviders(descriptor: ResourceDescriptor): List<ResourceProvider> {
        val result = Lists.newLinkedList<ResourceProvider>()
        for (provider in providers) {
            if (provider.handles(descriptor)) {
                result.add(provider)
            }
        }

        return result
    }

    private fun bindProviders(provider: ResourceProvider) {
        providers.add(provider)
    }

    private fun unbindProviders(provider: ResourceProvider) {
        providers.remove(provider)
    }

}
