package com.neva.javarel.storage.repository.api

open class RepositoryException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
