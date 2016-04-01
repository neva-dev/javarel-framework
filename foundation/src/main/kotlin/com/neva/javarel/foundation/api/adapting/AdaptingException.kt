package com.neva.javarel.foundation.api.adapting

open class AdaptingException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
