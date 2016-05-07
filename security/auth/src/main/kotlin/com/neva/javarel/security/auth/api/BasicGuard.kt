package com.neva.javarel.security.auth.api

open class BasicGuard(val authenticableProvider: AuthenticableProvider) : Guard {

    protected var authenticated: Authenticable? = null

    override val isAuthenticated: Boolean
        get() = authenticated != null

    override val authenticable: Authenticable
        get() = authenticated ?: authenticableProvider.guest

    override fun authenticate(authenticable: Authenticable) {
        this.authenticated = authenticable
    }

    override fun attempt(credentials: Credentials): Boolean {
        return authenticableProvider.byCredentials(credentials) != null
    }
}