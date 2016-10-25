package com.neva.javarel.security.auth.api

interface Guard {

    /**
     * Determine if the current user is authenticated.
     */
    val check: Boolean

    /**
     * Get the currently authenticated user or guest account
     */
    val user: Authenticable

    /**
     * Set the current user.
     */
    fun login(authenticable: Authenticable, remember: Boolean = false)

    /**
     * Unset current user.
     */
    fun logout()

    /**
     * Attempt to authenticate using specified credentials.
     */
    fun attempt(credentials: Credentials): Boolean

    /**
     * Validate only that specified credentials allows to authenticate.
     */
    fun validate(credentials: Credentials): Boolean

}