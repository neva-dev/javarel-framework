package com.neva.javarel.foundation.api.http

import com.google.common.base.Splitter
import com.google.common.collect.Maps

class QueryParams(val values: MutableMap<String, String> = mutableMapOf()) {

    companion object {
        fun fromMap(values: Map<String, Any>): QueryParams {
            return QueryParams(values.entries.fold(mutableMapOf<String, String>(), { map, entry ->
                map.put(entry.key, entry.value.toString()); map
            }))
        }

        fun fromString(str: String): QueryParams {
            if (str.isNullOrBlank()) {
                return QueryParams()
            }

            val values = Maps.newLinkedHashMap<String, String>()

            val fragments = Splitter.on("#").trimResults().splitToList(str)
            val params = fragments[1]

            if (fragments.size == 2) {
                values["#"] = fragments[1]
            }

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

            return QueryParams(values)
        }
    }

    override fun toString(): String {
        var result = ""

        val tmp = mutableMapOf<String, String>()
        tmp.putAll(values)

        val hash = tmp.remove("#")

        if (tmp.isNotEmpty()) {
            val query = tmp.entries.fold(mutableListOf<String>(), { list, e ->
                list.add(if (e.value.isNullOrBlank()) e.key else e.key + "=" + e.value); list
            }).joinToString("&")

            result += "?$query"
        }

        if (!hash.isNullOrBlank()) {
            result += "#" + hash
        }

        return result
    }

}