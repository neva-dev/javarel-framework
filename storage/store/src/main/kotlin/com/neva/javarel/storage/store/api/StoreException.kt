package com.neva.javarel.storage.store.api

open class StoreException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
