package com.neva.javarel.resource.api

import com.neva.javarel.foundation.api.JavarelException

open class ResourceException : JavarelException {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
