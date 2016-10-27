package com.neva.javarel.security.auth.api

import com.neva.javarel.security.auth.api.Authenticable

class PrincipalPasswordAuthenticable(override val principal: String, val password : String) : Authenticable