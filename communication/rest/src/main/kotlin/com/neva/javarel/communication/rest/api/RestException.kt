package com.neva.javarel.communication.rest.api

import com.neva.javarel.foundation.api.JavarelException

class RestException : JavarelException {

    constructor(message: String) : super(message)

}