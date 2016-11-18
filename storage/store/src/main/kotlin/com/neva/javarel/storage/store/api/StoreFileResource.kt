package com.neva.javarel.storage.store.api

import com.mongodb.gridfs.GridFSDBFile
import com.neva.javarel.resource.api.AdaptableResource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceResolver
import java.io.InputStream

class StoreFileResource(
        val file: GridFSDBFile,
        override val descriptor: ResourceDescriptor,
        override val resolver: ResourceResolver
) : AdaptableResource() {

    companion object {
        const val PROTOCOL = "store-file"

        fun uri(connection: String, fileStore: String, fileId: String): String {
            return "${PROTOCOL}://$connection/$fileStore/$fileId"
        }
    }

    override val input: InputStream
        get() = file.inputStream

}