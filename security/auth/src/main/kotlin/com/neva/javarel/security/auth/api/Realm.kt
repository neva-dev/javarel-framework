package com.neva.javarel.security.auth.api

interface Realm : AuthenticableProvider {

    val priority : Int

    fun supports(credentials: Credentials): Boolean

}