package com.neva.javarel.storage.repository.api

import org.mongodb.morphia.Datastore

interface Repository {

    val connection: RepositoryConnection

    val dataStore : Datastore

}