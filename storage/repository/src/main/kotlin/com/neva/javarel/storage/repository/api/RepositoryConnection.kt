package com.neva.javarel.storage.repository.api

import com.mongodb.MongoClient

interface RepositoryConnection {

    val name : String

    val client : MongoClient

    val dbName : String

}