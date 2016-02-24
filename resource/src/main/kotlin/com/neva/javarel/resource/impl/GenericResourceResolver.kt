package com.neva.javarel.resource.impl

import com.google.common.collect.Lists
import com.neva.javarel.resource.api.*
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import java.util.*

@Component
@Provides
@Instantiate
class GenericResourceResolver : ResourceResolver {

    @Requires(specification = ResourceProvider::class)
    lateinit var providers: List<ResourceProvider>

    @Requires(specification = ResourceAdapter::class)
    lateinit var adapters: List<ResourceAdapter>

    override fun find(uri: String): Resource? {
        val descriptor = ResourceDescriptor(uri)
        return provideResource(descriptor, findProviders(descriptor))
    }

    override fun resolve(uri: String): Resource {
        val descriptor = ResourceDescriptor(uri)
        val providers = findProviders(descriptor)

        if (providers.isEmpty()) {
            throw ResourceException("Cannot find any provider for resource: '${descriptor.uri}'")
        }

        val resource = provideResource(descriptor, providers)
        if (resource == null) {
            throw ResourceException("Resource by URI cannot be found: '${descriptor.uri}'")
        }

        return resource
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> adapt(resource: Resource, clazz: Class<T>): T {
        val adapter = findAdapter(clazz)
        if (adapter == null) {
            throw ResourceException("There is no valid resource adapter for class: '${clazz}'")
        }

        return adapter.adapt(resource) as T
    }

    override fun isAdaptable(clazz: Class<Any>): Boolean {
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

    private fun <T> findAdapter(clazz: Class<T>): ResourceAdapter? {
        for (adapter in adapters) {
            if (Objects.equals(clazz, adapter.type)) {
                return adapter
            }
        }

        return null
    }

}
