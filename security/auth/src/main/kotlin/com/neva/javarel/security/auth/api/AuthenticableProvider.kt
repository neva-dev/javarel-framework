package com.neva.javarel.security.auth.api

interface AuthenticableProvider {

    fun byIdentifier(identifier: String): Authenticable?

    fun byCredentials(credentials: Credentials): Authenticable?

    val guest: Authenticable

}