package com.neva.javarel.security.auth.api

interface Guard {

    /**
     * Determine if the current user is authenticated.
     */
    val isAuthenticated: Boolean

    /**
     * Get the currently authenticated user or guest account
     */
    val authenticable: Authenticable

    /**
     * Set the current user.
     */
    fun authenticate(authenticable: Authenticable);

    /**
     * Validate if there is some user which can use following credentials to authenticate.
     */
    fun attempt(credentials: Credentials): Boolean;

}