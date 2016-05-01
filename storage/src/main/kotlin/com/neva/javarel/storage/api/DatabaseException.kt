package com.neva.javarel.storage.api

open class DatabaseException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
