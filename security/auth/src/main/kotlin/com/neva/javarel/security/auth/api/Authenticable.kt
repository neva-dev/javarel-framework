package com.neva.javarel.security.auth.api

interface Authenticable {

    val authIdentifier: String

    val authPassword: String

}