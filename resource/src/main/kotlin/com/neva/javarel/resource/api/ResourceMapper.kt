package com.neva.javarel.resource.api

object ResourceMapper {

    val PROTOCOL_SEPARATOR = "://"

    val PATH_SEPARATOR = "/"

    fun uriToPath(uri: String): String {
        return uri.trim().replaceFirst(PROTOCOL_SEPARATOR, PATH_SEPARATOR)
    }

    fun pathToUri(path: String): String {
        return path.trim().replaceFirst(PATH_SEPARATOR, PROTOCOL_SEPARATOR)
    }

    fun fixUri(uri: String): String {
        return if (uri.contains(PROTOCOL_SEPARATOR)) uri else pathToUri(uri)
    }

}
