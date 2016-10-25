package com.neva.javarel.security.auth.api

/**
 * Designed for special accounts such us guest, super user etc.
 * Forces guard login without specifying any passwords. Used internally when session is active and user is logged in.
 */
class PrincipalCredentials(override val principal: String, override val remember: Boolean = false) : Credentials