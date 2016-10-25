package com.neva.javarel.security.auth.api

interface AuthenticableProvider {

    fun byCredentials(credentials: Credentials): Authenticable?

}