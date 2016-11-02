package com.neva.javarel.storage.store.api

import com.mongodb.MongoClient

interface StoreConnection {

    val name : String

    val client : MongoClient

    val dbName : String

}