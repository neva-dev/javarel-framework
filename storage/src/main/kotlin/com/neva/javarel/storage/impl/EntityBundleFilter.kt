package com.neva.javarel.storage.impl

import com.neva.javarel.foundation.api.osgi.BundleFilter
import com.neva.javarel.foundation.api.osgi.BundleUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOCase
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.objectweb.asm.ClassReader
import org.osgi.framework.Bundle
import javax.persistence.Entity

@Service(EntityBundleFilter::class, BundleFilter::class)
@Component
class EntityBundleFilter : BundleFilter {

    companion object {
        const val bundleHeader = "Database-Entity"
    }

    private val classes = mutableMapOf<Long, Collection<Class<*>>>()

    override fun checkBundle(bundle: Bundle): Boolean {
        return !getHeader(bundle).isNullOrBlank()
    }

    private fun getHeader(bundle: Bundle) = bundle.headers.get(bundleHeader)

    override fun checkClass(bundle: Bundle, classReader: ClassReader): Boolean {
        return isValidPackage(bundle, classReader) && hasEntityAnnotation(classReader);
    }

    private fun hasEntityAnnotation(classReader: ClassReader): Boolean {
        return BundleUtils.isAnnotated(classReader, Entity::class.java)
    }

    private fun isValidPackage(bundle: Bundle, classReader: ClassReader) = FilenameUtils.wildcardMatch(BundleUtils.toClassName(classReader), getHeader(bundle), IOCase.INSENSITIVE)

    override fun updateClasses(bundle: Bundle, classes: Collection<Class<*>>) {
        this.classes[bundle.bundleId] = classes
    }

    val entityClasses: Collection<Class<*>>
        get() {
            return classes.entries.fold(mutableListOf<Class<*>>(), { flattenedClasses, classes ->
                flattenedClasses.addAll(classes.value); flattenedClasses;
            }).toList();
        }
}