package com.neva.javarel.storage.repository.impl

import com.mongodb.gridfs.GridFSDBFile
import com.neva.javarel.resource.api.AdaptableResource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceResolver
import java.io.InputStream

class RepositoryFileResource(
        val file: GridFSDBFile,
        override val descriptor: ResourceDescriptor,
        override val resolver: ResourceResolver
) : AdaptableResource() {

    override val input: InputStream
        get() = file.inputStream

}