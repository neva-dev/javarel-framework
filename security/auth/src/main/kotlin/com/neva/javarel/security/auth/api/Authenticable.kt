package com.neva.javarel.security.auth.api

interface Authenticable {

    fun getAuthIdentifier();

    fun getAuthPassword();

}