package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.error.LoaderException
import com.mitchellbosecke.pebble.loader.Loader
import com.neva.javarel.resource.api.ResourceException
import com.neva.javarel.resource.api.ResourceResolver
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader

class PebbleLoader(val resourceResolver: ResourceResolver) : Loader<String> {

    companion object {
        val charset = "UTF-8"
    }

    override fun getReader(template: String): Reader {
        try {
            val resource = resourceResolver.findOrFail(template)

            return BufferedReader(InputStreamReader(resource.inputStream, charset))
        } catch (e: ResourceException) {
            throw LoaderException(e, "Cannot read a template: '$template")
        } catch (e: IOException) {
            throw LoaderException(e, "Cannot read a template: '$template'")
        }
    }

    override fun resolveRelativePath(relativePath: String?, anchorPath: String?): String? {
        return relativePath
    }

    override fun createCacheKey(path: String?): String? {
        return path
    }

    override fun setCharset(charset: String) {
        // nothing to do
    }

    override fun setPrefix(prefix: String) {
        // nothing to do
    }

    override fun setSuffix(suffix: String) {
        // nothing to do
    }


}
