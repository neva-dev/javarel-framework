package com.neva.javarel.foundation.api.scanning

import org.osgi.framework.BundleContext

interface BundleScanner {

    fun scan(filter: BundleFilter): Set<Class<*>>

    val context : BundleContext

}