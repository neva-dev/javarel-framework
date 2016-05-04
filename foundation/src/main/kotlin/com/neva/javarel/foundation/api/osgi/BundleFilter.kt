package com.neva.javarel.foundation.api.osgi

import org.objectweb.asm.ClassReader
import org.osgi.framework.Bundle

interface BundleFilter {

    fun checkBundle(bundle: Bundle): Boolean

    fun checkClass(bundle: Bundle, classReader: ClassReader): Boolean

    fun updateClasses(bundle: Bundle, classes: Collection<Class<*>>)

}