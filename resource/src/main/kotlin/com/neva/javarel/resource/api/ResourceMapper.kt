package com.neva.javarel.resource.api

import org.apache.commons.lang3.StringUtils

object ResourceMapper {

    val PROTOCOL_SEPARATOR = "://"

    val PATH_SEPARATOR = "/"

    fun uriToPath(uri: String): String {
        return StringUtils.trimToEmpty(uri).replaceFirst(PROTOCOL_SEPARATOR, PATH_SEPARATOR)
    }

    fun pathToUri(path: String): String {
        return StringUtils.trimToEmpty(path).replaceFirst(PATH_SEPARATOR, PROTOCOL_SEPARATOR)
    }

    fun fixUri(uri: String): String {
        return if (uri.contains(PROTOCOL_SEPARATOR)) uri else pathToUri(uri)
    }

}
