package com.neva.javarel.security.auth.api

interface AuthManager {

    fun guard(what: Any): Guard

}