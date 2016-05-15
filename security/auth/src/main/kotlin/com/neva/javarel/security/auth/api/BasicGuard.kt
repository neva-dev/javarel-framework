package com.neva.javarel.security.auth.api

open class BasicGuard(val authenticableProvider: AuthenticableProvider) : Guard {

    protected var authenticated: Authenticable? = null

    override val isAuthenticated: Boolean
        get() = authenticated != null

    override val authenticable: Authenticable
        get() = authenticated ?: authenticableProvider.guest

    override fun login(authenticable: Authenticable) {
        this.authenticated = authenticable
    }

    override fun logout() {
        this.authenticated = null
    }

    override fun attempt(credentials: Credentials): Boolean {
        val authenticable = authenticableProvider.byCredentials(credentials)
        if (authenticable != null) {
            login(authenticable)

            return true
        }

        return false
    }

    override fun validate(credentials: Credentials): Boolean {
        return authenticableProvider.byCredentials(credentials) != null
    }
}