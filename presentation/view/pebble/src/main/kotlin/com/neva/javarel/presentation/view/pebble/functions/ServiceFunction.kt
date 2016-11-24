package com.neva.javarel.presentation.view.pebble.functions

import com.neva.javarel.foundation.api.osgi.OsgiUtils
import com.neva.javarel.presentation.view.api.ViewException

class ServiceFunction(val osgiUtils: OsgiUtils) : BaseFunction() {

    companion object {
        const val CLASS_ARG = "class"
    }

    override fun getArgumentNames(): MutableList<String> {
        return mutableListOf(CLASS_ARG)
    }

    override fun execute(args: Map<String, Any>): Any {
        val serviceClass = args.get(CLASS_ARG) as String?
        if (serviceClass.isNullOrBlank()) {
            throw ViewException("OSGi service class cannot be empty.")
        }

        val service = osgiUtils.serviceOf<Any>(serviceClass!!)
        if (service == null) {
            throw ViewException("OSGi service '$serviceClass' cannot be found.")
        }

        return service
    }

}