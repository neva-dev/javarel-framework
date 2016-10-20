package com.neva.javarel.foundation.api

object JavarelConstants {

    const val VENDOR_NAME = "Neva Development"

    const val PRODUCT_NAME = "Javarel"

    const val SERVICE_PREFIX = "${PRODUCT_NAME} "

    const val VERSION = "1.0.0.ALPHA"

    fun asMap(): Map<String, String> {
        val that = this
        return javaClass.declaredFields.fold(mutableMapOf<String, String>(), { result, field ->
            result.put(field.name, field.get(that) as String); result
        })
    }

}