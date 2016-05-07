package com.neva.javarel.foundation.api.osgi

import org.osgi.framework.BundleContext
import org.osgi.framework.FrameworkUtil
import org.osgi.framework.ServiceReference

class OsgiUtils {

    val bundleContext: BundleContext

    constructor(clazz: Class<*>) {
        bundleContext = contextOf(clazz)
    }

    constructor() {
        bundleContext = contextOf(javaClass)
    }

    fun contextOf(clazz: Class<*>) = FrameworkUtil.getBundle(clazz).bundleContext

    @Suppress("UNCHECKED_CAST")
    fun <T> serviceOf(serviceInterface: String): T? {
        var service: T? = null

        val reference = bundleContext.getServiceReference(serviceInterface)
        if (reference != null) {
            service = bundleContext.getService(reference as ServiceReference<T>)
        }

        return service
    }

    fun <T> serviceOf(serviceInterface: Class<T>): T? {
        var service: T? = null

        val reference = bundleContext.getServiceReference(serviceInterface)
        if (reference != null) {
            service = bundleContext.getService(reference)
        }

        return service
    }

}