package com.neva.javarel.resource.impl

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.neva.javarel.resource.api.*
import org.apache.felix.scr.annotations.*
import java.util.*
import kotlin.reflect.KClass

@Component
@Service
class GenericResourceResolver : ResourceResolver {

    @Reference(referenceInterface = ResourceProvider::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var providers = Sets.newConcurrentHashSet<ResourceProvider>()

    @Reference(referenceInterface = ResourceAdapter::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var adapters = Sets.newConcurrentHashSet<ResourceAdapter<Any>>()

    override fun find(uri: String): Resource? {
        val descriptor = ResourceDescriptor(uri)
        return provideResource(descriptor, findProviders(descriptor))
    }

    override fun resolve(uri: String): Resource {
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

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> adapt(resource: Resource, clazz: KClass<T>): T {
        val adapter = findAdapter(clazz)
        if (adapter == null) {
            throw ResourceException("There is no valid resource adapter for class: '${clazz}'")
        }

        return adapter.adapt(resource) as T
    }

    override fun isAdaptable(clazz: KClass<Any>): Boolean {
        return findAdapter(clazz) != null
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

    private fun <T : Any> findAdapter(clazz: KClass<T>): ResourceAdapter<Any>? {
        for (adapter in adapters) {
            if (Objects.equals(clazz, adapter.type)) {
                return adapter
            }
        }

        return null
    }

    private fun bindProviders(provider: ResourceProvider) {
        providers.add(provider)
    }

    private fun unbindProviders(provider: ResourceProvider) {
        providers.remove(provider)
    }

    private fun bindAdapters(adapter: ResourceAdapter<Any>) {
        adapters.add(adapter)
    }

    private fun unbindAdapters(adapter: ResourceAdapter<Any>) {
        adapters.remove(adapter)
    }

}
