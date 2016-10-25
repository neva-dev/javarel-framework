package com.neva.javarel.security.auth.api

import java.io.Serializable

interface Credentials : Serializable {

    val principal: String

    val remember: Boolean

}