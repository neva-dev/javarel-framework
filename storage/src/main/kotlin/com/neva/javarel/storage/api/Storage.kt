package com.neva.javarel.storage.api

interface Storage {

    fun db(): Database

    fun db(connectionName: String): Database

}