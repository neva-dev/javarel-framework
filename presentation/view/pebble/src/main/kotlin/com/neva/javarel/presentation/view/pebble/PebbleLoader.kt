package com.neva.javarel.presentation.view.pebble

import com.mitchellbosecke.pebble.error.LoaderException
import com.mitchellbosecke.pebble.loader.Loader
import com.neva.javarel.resource.api.ResourceException
import com.neva.javarel.resource.api.ResourceResolver
import java.io.IOException
import java.io.Reader
import java.nio.charset.Charset

class PebbleLoader(val resourceResolver: ResourceResolver) : Loader<String> {

    companion object {
        val CHARSET = "UTF-8"
    }

    override fun getReader(template: String): Reader {
        try {
            val resource = resourceResolver.findOrFail(template)

            return resource.input.bufferedReader(Charset.forName(CHARSET))
        } catch (e: ResourceException) {
            throw LoaderException(e, "Cannot read a template: '$template")
        } catch (e: IOException) {
            throw LoaderException(e, "Cannot read a template: '$template'")
        }
    }

    /**
     * TODO Implement relative paths support in Pebble templates
     */
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
