package com.neva.javarel.resource.impl

import com.neva.javarel.resource.api.AdaptableResource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceException
import com.neva.javarel.resource.api.ResourceResolver

import java.io.IOException
import java.io.InputStream
import java.net.URL

class BundleResource(override val resolver: ResourceResolver, override val descriptor: ResourceDescriptor, private val url: URL) : AdaptableResource() {

    override val input: InputStream
        get() {
            try {
                return url.openStream()
            } catch (e: IOException) {
                throw ResourceException("Cannot read bundle resource input using URL: '$url'")
            }
        }
}
