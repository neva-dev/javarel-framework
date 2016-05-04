package com.neva.javarel.storage.impl

import com.neva.javarel.foundation.api.osgi.BundleFilter
import com.neva.javarel.foundation.api.osgi.BundleUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOCase
import org.objectweb.asm.ClassReader
import org.osgi.framework.Bundle
import javax.persistence.Entity

class EntityBundleFilter : BundleFilter {

    companion object {
        const val bundleHeader = "Database-Entity"
    }

    private val classes = mutableMapOf<Long, Collection<Class<*>>>()

    override fun filterBundle(bundle: Bundle): Boolean {
        return !getHeader(bundle).isNullOrBlank()
    }

    private fun getHeader(bundle: Bundle) = bundle.headers.get(bundleHeader)

    override fun filterClass(bundle: Bundle, classReader: ClassReader): Boolean {
        return isValidPackage(bundle, classReader) && hasEntityAnnotation(classReader);
    }

    private fun hasEntityAnnotation(classReader: ClassReader): Boolean {
        return BundleUtils.isAnnotated(classReader, Entity::class.java)
    }

    private fun isValidPackage(bundle: Bundle, classReader: ClassReader): Boolean {
        return FilenameUtils.wildcardMatch(BundleUtils.toClassName(classReader), getHeader(bundle), IOCase.INSENSITIVE)
    }
}