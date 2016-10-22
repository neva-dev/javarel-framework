package com.neva.javarel.foundation.api.lang

import com.google.common.collect.Maps

/**
 * Provides useful methods for operating on e.g deserialized JSON objects. Notice that all nested maps should
 * have equal key type - string.
 */
class MultimapHelper(val pathSeparator: String = ".") {

    /**
     * Combine two multi-level maps recursively.
     */
    @Suppress("UNCHECKED_CAST")
    fun extend(first: MutableMap<String, Any>, second: Map<String, Any>) {
        for ((key, value) in second) {

            if (value is Map<*, *>) {
                if (!first.containsKey(key)) {
                    first.put(key, Maps.newLinkedHashMap<String, Any>())
                }

                extend(first[key] as MutableMap<String, Any>, value as Map<String, Any>)
            } else {
                first.put(key, value)
            }
        }
    }

    /**
     * Get value from multi-level map.

     * @param path Keys sequence joined by '/' character)
     */
    @Suppress("UNCHECKED_CAST")
    operator fun get(map: Map<String, Any>, path: String): Any? {
        val parts = path.split(pathSeparator.toRegex()).dropLastWhile(String::isEmpty).toTypedArray()

        var current = map
        for (i in parts.indices) {
            val key = parts[i]
            if (i + 1 < parts.size) {
                if (!current.containsKey(key)) {
                    break
                }
                current = current[key] as Map<String, Any>
            } else {
                return current[key]
            }
        }

        return null
    }

    /**
     * Put value into multi-level map.

     * @param path Keys sequence joined by '/' character)
     */
    @Suppress("UNCHECKED_CAST")
    fun put(map: MutableMap<String, in Any>, path: String, value: Any) {
        val parts = path.split(pathSeparator.toRegex()).dropLastWhile(String::isEmpty).toTypedArray()

        var current: MutableMap<String, in Any> = map
        for (i in parts.indices) {
            val key = parts[i]
            if (i + 1 < parts.size) {
                if (!current.containsKey(key)) {
                    current.put(key, Maps.newLinkedHashMap<String, Any>())
                }
                current = current[key] as MutableMap<String, Any>
            } else {
                current.put(key, value)
            }
        }
    }

    /**
     * Remove value from nested multi-level map.

     * @param path Keys sequence joined by '/' character)
     */
    @Suppress("UNCHECKED_CAST")
    fun remove(map: MutableMap<String, in Any>, path: String): Boolean {
        val parts = path.split(pathSeparator.toRegex()).dropLastWhile(String::isEmpty).toTypedArray()

        var current: MutableMap<String, in Any> = map
        for (i in parts.indices) {
            val key = parts[i]
            if (i + 1 < parts.size) {
                if (!current.containsKey(key)) {
                    return false
                }
                current = current[key] as MutableMap<String, Any>
            } else if (current.containsKey(key)) {
                current.remove(key)
            }
        }

        return true
    }

    /**
     * Copy value (or reference!) from one multi-level map to another.

     * @param path Keys sequence joined by '/' character)
     */
    fun copy(source: Map<String, Any>, target: MutableMap<String, in Any>, path: String) {
        val value = get(source, path)
        if (value == null) {
            remove(target, path)
        } else {
            put(target, path, value)
        }
    }

    /**
     * Move value (or reference!) from one multi-level map to another.

     * @param path Keys sequence joined by '/' character)
     */
    fun move(source: MutableMap<String, Any>, target: MutableMap<String, Any>, path: String) {
        copy(source, target, path)
        remove(source, path)
    }

    /**
     * Find parent map by its child property value.
     */
    @Suppress("UNCHECKED_CAST")
    fun find(map: Map<String, Any>, property: String, value: Any): Map<String, Any> {
        var result: Map<String, Any> = mutableMapOf()
        for ((key, value1) in map) {
            if (map.containsKey(property) && map[property] == value) {
                return map
            }

            if (value1 is Map<*, *>) {
                result = find(value1 as Map<String, Any>, property, value)
            }
        }

        return result
    }
}// hidden constructor