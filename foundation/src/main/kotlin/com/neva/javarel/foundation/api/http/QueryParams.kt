package com.neva.javarel.foundation.api.http

import com.google.common.base.Splitter
import com.google.common.collect.Maps

class QueryParams {

    companion object {
        fun fromString(str: String): QueryParams {
            if (str.isNullOrBlank()) {
                return QueryParams()
            }

            var hash = ""

            val fragments = Splitter.on("#").trimResults().splitToList(str)
            val params = fragments[1]

            if (fragments.size == 2) {
                hash = fragments[1]
            }

            val values = Maps.newLinkedHashMap<String, Any>()
            for (args in Splitter.on("&").trimResults().splitToList(params)) {
                val pair = Splitter.on("=").trimResults().splitToList(args)
                val name = pair[0]

                if (pair.size == 1) {
                    values.put(name, "")
                } else if (pair.size == 2) {
                    val value = pair[1]

                    values.put(name, value)
                }
            }

            return QueryParams(values, hash)
        }
    }

    private var values = mutableMapOf<String, String>()

    private var hash: String = ""

    constructor(values: MutableMap<String, Any> = mutableMapOf(), hash: String = "") {
        set(values)
        set("#", hash)
    }

    override fun toString(): String {
        var result = ""

        if (values.isNotEmpty()) {
            result += "?" + values.entries.map({
                return if (it.value.isNullOrBlank()) it.key else it.key + "=" + it.value
            }).joinToString("&")
        }

        if (!hash.isNullOrBlank()) {
            result += "#" + hash
        }

        return result
    }

    fun set(name: String, value: Any) {
        set(name, value as String)
    }

    fun set(name: String, value: String) {
        if (name == "#") {
            hash = value
        } else {
            values[name] = value
        }
    }

    fun set(values: Map<String, Any>) {
        for ((name, value) in values) {
            set(name, value)
        }
    }

    fun get(name: String): String? {
        return values[name]
    }

}