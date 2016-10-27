package com.neva.javarel.security.auth.api

interface AuthConfig {

    val guest: PrincipalPasswordAuthenticable

    val admin: PrincipalPasswordAuthenticable

}