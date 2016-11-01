package com.neva.javarel.storage.repository.impl

import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceProvider
import com.neva.javarel.resource.api.ResourceResolver
import com.neva.javarel.storage.repository.api.RepositoryAdmin
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
        val file = repoAdmin.repository(getConnectionName(descriptor))
                .fileStore(getFileStore(descriptor))
                .findOne(ObjectId(getFileId(descriptor)))

        return RepositoryFileResource(file, descriptor, resolver)
    }

    private fun getConnectionName(descriptor: ResourceDescriptor) = descriptor.parts[0]

    private fun getFileStore(descriptor: ResourceDescriptor) = descriptor.parts[1]

    private fun getFileId(descriptor: ResourceDescriptor) = descriptor.parts[2]

}