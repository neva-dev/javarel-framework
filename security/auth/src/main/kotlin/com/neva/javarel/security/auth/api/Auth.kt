package com.neva.javarel.security.auth.api

interface Auth : Realm {

    val realms: Set<Realm>

    val guest: Authenticable

    val guestPrincipal: String

}