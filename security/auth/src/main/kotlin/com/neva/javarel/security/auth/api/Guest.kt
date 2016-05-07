package com.neva.javarel.security.auth.api

open class Guest : Authenticable {

    companion object {
        const val identifier = "guest"
        const val password = "guest"
    }

    override val authIdentifier: String
        get() = identifier

    override val authPassword: String
        get() = password
}