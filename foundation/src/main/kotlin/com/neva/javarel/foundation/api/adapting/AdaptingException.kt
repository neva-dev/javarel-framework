package com.neva.javarel.foundation.api.adapting

import com.neva.javarel.foundation.api.JavarelException

open class AdaptingException : JavarelException {

    constructor(message: String) : super(message)
}
