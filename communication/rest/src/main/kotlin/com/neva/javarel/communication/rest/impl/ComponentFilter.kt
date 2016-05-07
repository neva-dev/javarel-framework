package com.neva.javarel.communication.rest.impl

import com.neva.javarel.foundation.api.osgi.BundleFilter
import com.neva.javarel.foundation.api.osgi.BundleUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOCase
import org.objectweb.asm.ClassReader
import org.osgi.framework.Bundle
import javax.ws.rs.Path
import javax.ws.rs.ext.Provider

class ComponentFilter : BundleFilter {

    companion object {
        const val bundleHeader = "Database-Entity"
    }

    private val classes = mutableMapOf<Long, Collection<Class<*>>>()

    override fun filterBundle(bundle: Bundle): Boolean {
        return !getHeader(bundle).isNullOrBlank()
    }

    private fun getHeader(bundle: Bundle) = bundle.headers.get(bundleHeader)

    override fun filterClass(bundle: Bundle, classReader: ClassReader): Boolean {
        return isValidPackage(bundle, classReader) && hasComponentAnnotation(classReader);
    }

    private fun hasComponentAnnotation(classReader: ClassReader): Boolean {
        return BundleUtils.isAnnotated(classReader, setOf(Path::class.java, Provider::class.java))
    }

    private fun isValidPackage(bundle: Bundle, classReader: ClassReader): Boolean {
        return FilenameUtils.wildcardMatch(BundleUtils.toClassName(classReader), getHeader(bundle), IOCase.INSENSITIVE)
    }
}