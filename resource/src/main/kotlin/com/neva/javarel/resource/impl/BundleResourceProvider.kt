package com.neva.javarel.resource.impl

import com.google.common.collect.Maps
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceProvider
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext

@Component(immediate = true)
@Service
class BundleResourceProvider : ResourceProvider {

    companion object {
        val PROTOCOL = "bundle"
        val NAMESPACE_HEADER_NAME = "Resource-Namespace"
    }

    private val bundlesPerNamespace = Maps.newConcurrentMap<String, Bundle>()

    private lateinit var context: BundleContext

    @Activate
    protected fun start(context: BundleContext) {
        this.context = context
    }

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return descriptor.protocol == PROTOCOL
    }

    override fun provide(resolver: ResourceResolver, descriptor: ResourceDescriptor): Resource? {
        val bundle = findBundle(descriptor) ?: return null
        val resourcePath = getResourcePath(descriptor)
        val url = bundle.getEntry("/" + resourcePath)

        var resource: Resource? = null
        if (url != null) {
            resource = BundleResource(resolver, descriptor, url)
        }

        return resource
    }

    private fun findBundle(descriptor: ResourceDescriptor): Bundle? {
        val namespace = getResourceNamespace(descriptor)

        return bundlesPerNamespace.getOrPut(namespace, {
            context.bundles.find { bundle -> namespace == bundle.headers.get(NAMESPACE_HEADER_NAME) }
        })
    }

    private fun getResourcePath(descriptor: ResourceDescriptor) = descriptor.parts.subList(1, descriptor.parts.size).joinToString("/")

    private fun getResourceNamespace(descriptor: ResourceDescriptor) = descriptor.parts[0]
}
