package com.neva.javarel.storage.store.impl

import com.mongodb.gridfs.GridFS
import com.neva.javarel.resource.api.Resource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceProvider
import com.neva.javarel.resource.api.ResourceResolver
import com.neva.javarel.storage.store.api.StoreAdmin
import com.neva.javarel.storage.store.api.StoreFileResource
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.bson.types.ObjectId

@Component(immediate = true)
@Service
class StoreFileResourceProvider : ResourceProvider {

    @Reference
    private lateinit var repoAdmin: StoreAdmin

    companion object {
        val PROTOCOL = "store-file"
    }

    override fun handles(descriptor: ResourceDescriptor): Boolean {
        return descriptor.protocol == PROTOCOL
    }

    override fun provide(resolver: ResourceResolver, descriptor: ResourceDescriptor): Resource? {
        var result: Resource? = null

        val file = repoAdmin.store(getConnectionName(descriptor))
                .fileStore(getFileStore(descriptor))
                .findOne(ObjectId(getFileId(descriptor)))
        if (file != null) {
            result = StoreFileResource(file, descriptor, resolver)
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