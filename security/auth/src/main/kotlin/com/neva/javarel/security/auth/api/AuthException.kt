package com.neva.javarel.security.auth.api

import com.neva.javarel.foundation.api.JavarelException

open class AuthException : JavarelException {

    constructor(message: String) : super(message)

}