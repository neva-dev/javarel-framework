package com.neva.javarel.security.auth.api

class PrincipalPasswordAuthenticable(override val principal: String, val password : String) : Authenticable