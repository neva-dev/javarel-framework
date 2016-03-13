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

@Component
@Service
class BundleResourceProvider : ResourceProvider {

    companion object {
        val namespaceHeaderName = "Resource-Namespace"
    }

    private val foundBundles = Maps.newConcurrentMap<String, Bundle>()

    private lateinit var context: BundleContext

    @Activate
    protected fun start(context: BundleContext) {
        this.context = context
    }

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return findBundle(descriptor) != null
    }

    override fun provide(resolver: ResourceResolver, descriptor: ResourceDescriptor): Resource? {
        val bundle = findBundle(descriptor) ?: return null
        val resourcePath = getResourcePath(descriptor)
        val url = bundle.getEntry("/" + resourcePath)

        var resource: Resource? = null
        if (url != null) {
            resource = UrlResource(resolver, descriptor, url)
        }

        return resource
    }

    private fun findBundle(descriptor: ResourceDescriptor): Bundle? {
        val namespace = getResourceNamespace(descriptor)

        if (!foundBundles.containsKey(namespace)) {
            for (bundle in context.bundles) {
                if (namespace.equals(bundle.headers.get(namespaceHeaderName))) {
                    foundBundles.put(namespace, bundle)
                    break
                }
            }
        }

        return foundBundles[namespace]
    }

    private fun getResourcePath(descriptor: ResourceDescriptor) = descriptor.parts.subList(1, descriptor.parts.size).joinToString("/")

    private fun getResourceNamespace(descriptor: ResourceDescriptor) = descriptor.parts[0]
}
