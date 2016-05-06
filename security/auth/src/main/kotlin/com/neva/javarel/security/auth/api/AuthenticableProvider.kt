package com.neva.javarel.security.auth.api

interface AuthenticableProvider {

    fun byIdentifier(identifier: Any): Authenticable?

    fun byCredentials(credentials: Map<String, Any>): Authenticable?

}