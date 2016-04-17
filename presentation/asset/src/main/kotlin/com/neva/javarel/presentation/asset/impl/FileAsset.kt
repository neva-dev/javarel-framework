package com.neva.javarel.presentation.asset.impl

import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceAdaptee
import com.neva.javarel.resource.api.ResourceException
import com.neva.javarel.presentation.asset.api.Asset
import com.neva.javarel.presentation.asset.api.AssetException
import org.apache.tika.Tika

import java.io.IOException
import java.io.InputStream

open class FileAsset(resource: Resource) : ResourceAdaptee(resource), Asset {

    companion object {
        val TIKA = Tika()
    }

    override val mimeType: String
        get() {
            try {
                return TIKA.detect(inputStream, descriptor.name)
            } catch (e: ResourceException) {
                throw AssetException("Cannot determine mime type for a resource: '$resource'", e)
            } catch (e: IOException) {
                throw AssetException("Cannot determine mime type for a resource: '$resource'", e)
            }
        }

    override fun read(): InputStream {
        try {
            return inputStream
        } catch (e: ResourceException) {
            throw AssetException("Cannot read file asset from resource: '$resource'", e)
        }

    }

    override fun compile(): InputStream {
        return read()
    }

}
