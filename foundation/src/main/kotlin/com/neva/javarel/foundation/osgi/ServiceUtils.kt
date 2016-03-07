package com.neva.javarel.foundation.osgi

import org.osgi.framework.FrameworkUtil
import kotlin.reflect.KClass

class ServiceUtils {

    companion object {
        fun <T : Any> getService(clazz: KClass<T>): T {
            val bundleContext = FrameworkUtil.getBundle(ServiceUtils::class.java).bundleContext
            val ref = bundleContext.getServiceReference(clazz.java)

            if (ref != null) {
                return bundleContext.getService(ref)
            }

            throw IllegalAccessException("OSGi service '${clazz.simpleName}' is not available in current context.")
        }
    }

}