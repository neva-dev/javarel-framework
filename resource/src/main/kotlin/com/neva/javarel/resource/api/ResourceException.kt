package com.neva.javarel.resource.api

class ResourceException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
