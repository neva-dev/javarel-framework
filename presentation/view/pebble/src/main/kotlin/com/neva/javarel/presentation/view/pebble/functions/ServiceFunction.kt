package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.foundation.api.osgi.OsgiUtils
import com.neva.javarel.presentation.view.api.ViewException

class ServiceFunction(val osgiUtils: OsgiUtils) : BaseFunction() {

    override fun execute(args: Map<String, Any>): Any {
        val serviceClass = firstArgument(args) as String

        return osgiUtils.serviceOf(serviceClass) ?: throw ViewException("OSGi service '$serviceClass' cannot be found.")
    }

}