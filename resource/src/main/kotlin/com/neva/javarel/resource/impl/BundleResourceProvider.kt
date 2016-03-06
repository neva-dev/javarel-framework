package com.neva.javarel.resource.impl

import com.google.common.collect.Maps
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceProvider
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Context
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext

@Component(immediate = true)
@Provides
@Instantiate
class BundleResourceProvider : ResourceProvider {

    companion object {
        val namespaceHeaderName = "Resource-Namespace"
    }

    @Context
    lateinit var context: BundleContext

    private val bundles = Maps.newConcurrentMap<String, Bundle>()

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return getBundle(descriptor) != null
    }

    override fun provide(resolver: ResourceResolver, descriptor: ResourceDescriptor): Resource? {
        val bundle = getBundle(descriptor) ?: return null
        val resourcePath = getResourcePath(descriptor)
        val url = bundle.getEntry("/" + resourcePath)

        var resource: Resource? = null
        if (url != null) {
            resource = UrlResource(resolver, descriptor, url)
        }

        return resource
    }

    private fun getBundle(descriptor: ResourceDescriptor): Bundle? {
        val namespace = getResourceNamespace(descriptor)

        if (!bundles.containsKey(namespace)) {
            for (bundle in context.bundles) {
                if (namespace.equals(bundle.headers.get(namespaceHeaderName))) {
                    bundles.put(namespace, bundle)
                    break
                }
            }
        }

        return bundles[namespace]
    }

    private fun getResourcePath(descriptor: ResourceDescriptor) = descriptor.parts.subList(1, descriptor.parts.size).joinToString("/")

    private fun getResourceNamespace(descriptor: ResourceDescriptor) = descriptor.parts[0]
}
