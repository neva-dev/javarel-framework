package com.neva.javarel.security.auth.impl

import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.security.auth.api.*
import javax.inject.Inject

class SessionGuard : Guard {

    companion object {
        val PRINCIPAL_ATTR = "guard.authenticable.principal"
    }

    private val session: Session

    private val auth: Auth

    private lateinit var authenticated: Authenticable

    @Inject
    constructor(session: Session, @Uses auth: Auth) {
        this.session = session
        this.auth = auth
        this.authenticated = read()
    }

    /**
     * TODO Implement remember me support
     */
    override fun login(authenticable: Authenticable, remember: Boolean) {
        this.authenticated = authenticable

        session.set(PRINCIPAL_ATTR, authenticable.principal)
    }

    override fun logout() {
        this.authenticated = auth.guest

        session.remove(SessionGuard.PRINCIPAL_ATTR)
    }

    private fun read(): Authenticable {
        var authenticable: Authenticable? = null
        val principal = session.get(PRINCIPAL_ATTR) as String?
        if (!principal.isNullOrBlank()) {
            authenticable = auth.byCredentials(PrincipalCredentials(principal!!))
        }

        return authenticable ?: auth.guest
    }

    override val check: Boolean
        get() = user.principal != auth.guest.principal

    override val user: Authenticable
        get() = authenticated

    override fun attempt(credentials: Credentials): Boolean {
        val authenticable = auth.byCredentials(credentials)
        if (authenticable != null) {
            login(authenticable, credentials.remember)

            return true
        }

        return false
    }

    override fun validate(credentials: Credentials): Boolean {
        return auth.byCredentials(credentials) != null
    }
}