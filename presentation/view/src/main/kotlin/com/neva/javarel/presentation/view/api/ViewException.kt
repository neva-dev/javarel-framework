package com.neva.javarel.presentation.view.api

import com.neva.javarel.resource.api.ResourceException

open class ViewException : ResourceException {

    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)

}
