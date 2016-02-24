package com.neva.javarel.resource.api

import org.apache.commons.io.FilenameUtils

class ResourceDescriptor(val uri: String) {

    companion object {
        val PROTOCOL_SEPARATOR = ":"
    }

    val protocol: String

    val path: String

    val baseName: String

    val name: String

    val extension: String

    init {
        val parts = uri.split(PROTOCOL_SEPARATOR.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        if (parts.size == 1) {
            throw IllegalArgumentException(String.format("Resource descriptor URI '%s' should contain " + "protocol separator '%s'", uri, PROTOCOL_SEPARATOR))
        }

        this.protocol = parts[0]
        this.path = parts[1]
        this.baseName = FilenameUtils.getBaseName(parts[1])
        this.name = FilenameUtils.getName(parts[1])
        this.extension = FilenameUtils.getExtension(parts[1])
    }

    override fun toString(): String {
        return String.format("Resource descriptor '%s'", uri)
    }


}
