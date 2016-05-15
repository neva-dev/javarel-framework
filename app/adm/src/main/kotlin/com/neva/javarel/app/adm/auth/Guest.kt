package com.neva.javarel.app.adm.auth

import com.neva.javarel.security.auth.api.Authenticable

open class Guest : Authenticable {

    companion object {
        const val EMAIL = "guest@neva.zone"
        const val PASSWORD = "guest"
    }

    override val authIdentifier: String
        get() = EMAIL

    override val authPassword: String
        get() = PASSWORD
}