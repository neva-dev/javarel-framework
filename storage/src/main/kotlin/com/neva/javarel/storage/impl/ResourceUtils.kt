package com.neva.javarel.storage.impl

import com.neva.javarel.storage.api.ResourceDescriptor
import org.apache.commons.lang3.StringUtils

object ResourceUtils {

    fun getPath(uri: String): String {
        return StringUtils.trimToEmpty(uri).replaceFirst(ResourceDescriptor.PROTOCOL_SEPARATOR, "/")
    }

    fun getUri(path: String): String {
        return StringUtils.trimToEmpty(path).replaceFirst("/", ResourceDescriptor.PROTOCOL_SEPARATOR)
    }

}
