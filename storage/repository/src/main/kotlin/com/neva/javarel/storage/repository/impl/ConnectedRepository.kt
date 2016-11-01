package com.neva.javarel.storage.repository.impl

import com.mongodb.DB
import com.mongodb.gridfs.GridFS
import com.neva.javarel.storage.repository.api.Repository
import com.neva.javarel.storage.repository.api.RepositoryConnection
import org.mongodb.morphia.Datastore

class ConnectedRepository(
        override val connection: RepositoryConnection,
        override val dataStore: Datastore,
        override val db: DB = DB(dataStore.mongo, connection.dbName)
) : Repository {

    private val fileStores = mutableMapOf<String, GridFS>()

    override fun fileStore(name: String): GridFS {
        return fileStores.getOrPut(name, { GridFS(db, name) })
    }

    override val fileStore: GridFS
        get() = fileStore(GridFS.DEFAULT_BUCKET)

}