package com.neva.javarel.resource.api

import com.google.common.collect.ImmutableList
import org.apache.commons.io.FilenameUtils

class ResourceDescriptor(val uri: String) {

    val protocol: String

    val path: String

    val baseName: String

    val name: String

    val extension: String

    val parts: List<String>

    init {
        var parts = uri.split(ResourceMapper.PROTOCOL_SEPARATOR)
        if (parts.size != 2) {
            throw IllegalArgumentException("Resource descriptor URI '$uri' should contain " + "protocol separator '${ResourceMapper.PROTOCOL_SEPARATOR}'")
        }

        this.protocol = parts[0]
        this.path = parts[1]
        this.parts = ImmutableList.copyOf(path.split(ResourceMapper.PATH_SEPARATOR))
        this.baseName = FilenameUtils.getBaseName(parts[1])
        this.name = FilenameUtils.getName(parts[1])
        this.extension = FilenameUtils.getExtension(parts[1])
    }

    override fun toString(): String {
        return "Resource descriptor '$uri'"
    }


}
