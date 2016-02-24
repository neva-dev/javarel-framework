package com.neva.javarel.storage.impl

import com.google.common.collect.Maps
import com.neva.javarel.storage.api.Resource
import com.neva.javarel.storage.api.ResourceDescriptor
import com.neva.javarel.storage.api.ResourceProvider
import com.neva.javarel.storage.api.ResourceResolver
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext

@Component
@Provides
@Instantiate
class BundleResourceProvider : ResourceProvider {

    @Requires
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
