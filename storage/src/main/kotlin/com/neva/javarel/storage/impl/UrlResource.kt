package com.neva.javarel.storage.impl

import com.neva.javarel.storage.api.AdaptableResource
import com.neva.javarel.storage.api.ResourceDescriptor
import com.neva.javarel.storage.api.ResourceException
import com.neva.javarel.storage.api.ResourceResolver

import java.io.IOException
import java.io.InputStream
import java.net.URL

class UrlResource(override val resolver: ResourceResolver, override val descriptor: ResourceDescriptor, private val url: URL) : AdaptableResource() {

    override val inputStream: InputStream
        get() {
            try {
                return url.openStream()
            } catch (e: IOException) {
                throw ResourceException(String.format("Cannot read resource content using URL: '%s'", url))
            }

        }
}
