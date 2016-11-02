package com.neva.javarel.storage.store.api

import com.mongodb.gridfs.GridFSDBFile
import com.neva.javarel.resource.api.AdaptableResource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceResolver
import com.neva.javarel.storage.store.impl.StoreFileResourceProvider
import java.io.InputStream

class StoreFileResource(
        val file: GridFSDBFile,
        override val descriptor: ResourceDescriptor,
        override val resolver: ResourceResolver
) : AdaptableResource() {

    companion object {
        fun uri(connection: String, fileStore: String, fileId: String): String {
            return "${StoreFileResourceProvider.PROTOCOL}://$connection/$fileStore/$fileId"
        }
    }

    override val input: InputStream
        get() = file.inputStream

}