package com.neva.javarel.foundation.api.scanning

import com.neva.javarel.foundation.api.osgi.BundleUtils
import com.neva.javarel.foundation.api.osgi.ClassUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOCase
import org.objectweb.asm.ClassReader
import org.osgi.framework.Bundle

class ComponentScanBundleFilter(val annotations: Set<Class<out Annotation>>) : BundleFilter {

    companion object {
        const val BUNDLE_HEADER = "Component-Scan"
    }

    private val classes = mutableMapOf<Long, Collection<Class<*>>>()

    override fun filterBundle(bundle: Bundle): Boolean {
        return BundleUtils.isActive(bundle) && !getHeader(bundle).isNullOrBlank()
    }

    private fun getHeader(bundle: Bundle) = bundle.headers.get(BUNDLE_HEADER)

    override fun filterClass(bundle: Bundle, classReader: ClassReader): Boolean {
        return isValidPackage(bundle, classReader) && isAnnotated(classReader);
    }

    private fun isAnnotated(classReader: ClassReader): Boolean {
        return ClassUtils.isAnnotated(classReader, annotations)
    }

    private fun isValidPackage(bundle: Bundle, classReader: ClassReader): Boolean {
        return FilenameUtils.wildcardMatch(ClassUtils.toClassName(classReader), getHeader(bundle), IOCase.INSENSITIVE)
    }

}