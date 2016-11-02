package com.neva.javarel.storage.store.api

import com.mongodb.DB
import com.mongodb.gridfs.GridFS
import org.mongodb.morphia.Datastore

interface Store {

    val connection: StoreConnection

    val db: DB

    val dataStore: Datastore

    val fileStore: GridFS

    fun fileStore(name: String): GridFS

}