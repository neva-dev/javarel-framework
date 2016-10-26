package com.neva.javarel.security.auth.api

data class PrincipalPasswordCredentials(
        override val principal: String,
        val password: String,
        override val remember: Boolean = true
) : Credentials