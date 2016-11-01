package com.neva.javarel.storage.repository.api

import com.mongodb.gridfs.GridFSDBFile
import com.neva.javarel.resource.api.AdaptableResource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceResolver
import com.neva.javarel.storage.repository.impl.RepositoryFileResourceProvider
import org.bson.types.ObjectId
import java.io.InputStream

class RepositoryFileResource(
        val file: GridFSDBFile,
        override val descriptor: ResourceDescriptor,
        override val resolver: ResourceResolver
) : AdaptableResource() {

    companion object {
        fun uri(connection: String, fileStore: String, fileId: ObjectId): String {
            return uri(connection, fileStore, fileId.toHexString())
        }

        fun uri(connection: String, fileStore: String, fileId: String): String {
            return "${RepositoryFileResourceProvider.PROTOCOL}://$connection/$fileStore/$fileId"
        }
    }

    override val input: InputStream
        get() = file.inputStream

}