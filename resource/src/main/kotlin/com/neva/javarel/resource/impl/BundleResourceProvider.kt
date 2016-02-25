package com.neva.javarel.resource.impl

import com.google.common.collect.Maps
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceProvider
import com.neva.javarel.resource.api.ResourceResolver
import org.apache.felix.ipojo.annotations.*
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext

@Component(immediate = true)
@Provides
@Instantiate
class BundleResourceProvider : ResourceProvider {

    @Context
    lateinit var context: BundleContext

    private val bundles = Maps.newConcurrentMap<String, Bundle>()

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return getBundle(descriptor) != null
    }

    override fun provide(resolver: ResourceResolver, descriptor: ResourceDescriptor): Resource? {
        val bundle = getBundle(descriptor) ?: return null
        val url = bundle.getEntry("/" + descriptor.path)

        var resource: Resource? = null
        if (url != null) {
            resource = UrlResource(resolver, descriptor, url)
        }

        return resource
    }

    private fun getBundle(descriptor: ResourceDescriptor): Bundle? {
        val symbolicName = descriptor.protocol

        if (!bundles.containsKey(symbolicName)) {
            for (bundle in context.bundles) {
                if (symbolicName.equals(bundle.symbolicName, ignoreCase = true)) {
                    bundles.put(symbolicName, bundle)
                }
            }
        }

        return bundles[symbolicName]
    }


}
