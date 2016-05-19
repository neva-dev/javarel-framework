package com.neva.javarel.security.auth.api.guard

import com.neva.javarel.security.auth.api.Authenticable
import com.neva.javarel.security.auth.api.AuthenticableProvider
import com.neva.javarel.security.auth.api.BasicGuard
import javax.servlet.http.HttpServletRequest

/**
 * TODO Encode session and make session abstract (create drivers)
 */
class RequestGuard(val request: HttpServletRequest, authenticableProvider: AuthenticableProvider) : BasicGuard(authenticableProvider) {

    companion object {
        val sessionIdentifier = "authentication"
    }

    init {
        readFromSession()
    }

    override fun login(authenticable: Authenticable) {
        writeToSession(authenticable)

        super.login(authenticable)
    }

    override fun logout() {
        writeToSession(null)

        super.logout()
    }

    private fun readFromSession() {
        val identifier = (request.session.getAttribute(sessionIdentifier) as String).orEmpty()
        this.authenticated = authenticableProvider.byIdentifier(identifier) ?: authenticableProvider.guest
    }

    private fun writeToSession(authenticable: Authenticable?) {
        request.session.setAttribute(sessionIdentifier, authenticable?.authIdentifier ?: null)
    }
}