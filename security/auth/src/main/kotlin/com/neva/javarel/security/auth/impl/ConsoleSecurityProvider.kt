package com.neva.javarel.security.auth.impl

import com.neva.javarel.security.auth.api.Auth
import com.neva.javarel.security.auth.api.PrincipalPasswordCredentials
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.apache.felix.webconsole.WebConsoleSecurityProvider
import org.apache.felix.webconsole.WebConsoleSecurityProvider3
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.HttpHeaders

@Component(immediate = true)
@Service(WebConsoleSecurityProvider::class)
class ConsoleSecurityProvider : WebConsoleSecurityProvider3 {

    @Reference
    private lateinit var auth: Auth

    override fun authenticate(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        return authenticateUsingBasic(request) || authenticateUsingSession(request)
    }

    private fun authenticateUsingBasic(request: HttpServletRequest): Boolean {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header.isNullOrBlank()) {
            return false
        }

        val (principal, password) = String(Base64.getDecoder().decode(header.substring(6))).split(":").toList()
        val authenticable = auth.byCredentials(PrincipalPasswordCredentials(principal, password))

        return authenticable != null
    }

    private fun authenticateUsingSession(request: HttpServletRequest): Boolean {
        val session = RequestSession(request.session)
        val guard = SessionGuard(session, auth)

        return guard.check
    }

    override fun authenticate(username: String, password: String): Any? {
        return auth.byCredentials(PrincipalPasswordCredentials(username, password))
    }

    override fun authorize(user: Any?, role: String?): Boolean {
        return true
    }

    override fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        val session = RequestSession(request.session)
        val guard = SessionGuard(session, auth)

        guard.logout()
    }
}