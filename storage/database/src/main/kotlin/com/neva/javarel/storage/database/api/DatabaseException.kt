package com.neva.javarel.storage.database.api

open class DatabaseException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
