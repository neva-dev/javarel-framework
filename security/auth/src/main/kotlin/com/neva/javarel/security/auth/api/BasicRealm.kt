package com.neva.javarel.security.auth.api

abstract class BasicRealm : Realm {

    override fun supports(credentials: Credentials): Boolean {
        return (credentials is PrincipalPasswordCredentials) || (credentials is PrincipalCredentials)
    }

}