package com.neva.javarel.storage.store.impl

import com.mongodb.DB
import com.mongodb.gridfs.GridFS
import com.neva.javarel.storage.store.api.Store
import com.neva.javarel.storage.store.api.StoreConnection
import org.mongodb.morphia.Datastore

class ConnectedStore(
        override val connection: StoreConnection,
        override val dataStore: Datastore,
        override val db: DB = DB(dataStore.mongo, connection.dbName)
) : Store {

    private val fileStores = mutableMapOf<String, GridFS>()

    override fun fileStore(name: String): GridFS {
        return fileStores.getOrPut(name, { GridFS(db, name) })
    }

    override val fileStore: GridFS
        get() = fileStore(GridFS.DEFAULT_BUCKET)

}