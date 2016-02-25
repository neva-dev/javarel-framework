package com.neva.javarel.resource.api

open class ResourceException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
