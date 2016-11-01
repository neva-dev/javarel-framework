package com.neva.javarel.storage.repository.api

import com.mongodb.DB
import com.mongodb.gridfs.GridFS
import org.mongodb.morphia.Datastore

interface Repository {

    val connection: RepositoryConnection

    val db: DB

    val dataStore: Datastore

    val fileStore: GridFS

    fun fileStore(name: String): GridFS

}