package com.neva.javarel.storage.api

interface DatabaseAdmin {

    fun database(): Database

    fun database(connectionName: String): Database

    val connections: Set<DatabaseConnection>

    val connectedDatabases: Set<Database>

}