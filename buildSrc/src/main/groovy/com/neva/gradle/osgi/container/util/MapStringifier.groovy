package com.neva.gradle.osgi.container.util

class MapStringifier {

    static String asProperties(Map<String, Object> map) {
        def buffer = new StringBuffer()
        map.each { key, value -> buffer.append("$key=$value\n") }
        buffer.toString()
    }
}

