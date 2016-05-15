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
    fun login(authenticable: Authenticable);

    /**
     * Unset current user.
     */
    fun logout();

    /**
     * Attempt to authenticate using specified credentials.
     */
    fun attempt(credentials: Credentials): Boolean;

    /**
     * Validate only that specified credentials allows to authenticate.
     */
    fun validate(credentials: Credentials): Boolean;

}