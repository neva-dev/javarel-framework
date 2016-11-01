package com.neva.javarel.storage.repository.impl

import com.mongodb.gridfs.GridFS
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceProvider
import com.neva.javarel.resource.api.ResourceResolver
import com.neva.javarel.storage.repository.api.RepositoryAdmin
import com.neva.javarel.storage.repository.api.RepositoryFileResource
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.bson.types.ObjectId

@Component(immediate = true)
@Service
class RepositoryFileResourceProvider : ResourceProvider {

    @Reference
    private lateinit var repoAdmin: RepositoryAdmin

    companion object {
        val PROTOCOL = "repository-file"
    }

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return descriptor.protocol == PROTOCOL
    }

    override fun provide(resolver: ResourceResolver, descriptor: ResourceDescriptor): Resource? {
        var result: Resource? = null

        val file = repoAdmin.repository(getConnectionName(descriptor))
                .fileStore(getFileStore(descriptor))
                .findOne(ObjectId(getFileId(descriptor)))
        if (file != null) {
            result = RepositoryFileResource(file, descriptor, resolver)
        }

        return result
    }

    private fun getConnectionName(descriptor: ResourceDescriptor) = when (descriptor.parts.size) {
        3 -> descriptor.parts[0]
        else -> repoAdmin.connectionDefault
    }

    private fun getFileStore(descriptor: ResourceDescriptor) = when (descriptor.parts.size) {
        3 -> descriptor.parts[1]
        2 -> descriptor.parts[0]
        else -> GridFS.DEFAULT_BUCKET
    }

    private fun getFileId(descriptor: ResourceDescriptor) = when (descriptor.parts.size) {
        3 -> descriptor.parts[2]
        2 -> descriptor.parts[1]
        else -> descriptor.parts[0]
    }

}