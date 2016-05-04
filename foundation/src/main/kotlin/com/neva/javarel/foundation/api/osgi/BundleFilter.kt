package com.neva.javarel.foundation.api.osgi

import org.objectweb.asm.ClassReader
import org.osgi.framework.Bundle

interface BundleFilter {

    fun filterBundle(bundle: Bundle): Boolean

    fun filterClass(bundle: Bundle, classReader: ClassReader): Boolean

}