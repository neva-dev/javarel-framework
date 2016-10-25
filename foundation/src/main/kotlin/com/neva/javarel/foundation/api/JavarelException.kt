package com.neva.javarel.foundation.api

open class JavarelException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}