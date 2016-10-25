package com.neva.javarel.security.auth.api

interface Realm : AuthenticableProvider {

    fun supports(credentials: Credentials): Boolean

}