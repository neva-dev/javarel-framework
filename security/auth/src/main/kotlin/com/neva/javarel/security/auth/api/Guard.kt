package com.neva.javarel.security.auth.api

interface Guard {

    /**
     * Determine if the current user is authenticated.
     */
    val authenticated: Boolean

    /**
     * Get the currently authenticated user.
     */
    val authenticable: Authenticable

    /**
     * Set the current user.
     */
    fun authenticate(authenticable: Authenticable);

    /**
     * Validate if there is some user which can use following credentials to authenticate.
     */
    fun canAuthenticate(credentials: Credentials): Boolean;

}