package com.neva.javarel.security.auth.impl

import com.neva.javarel.security.auth.api.Authenticable
import com.neva.javarel.security.auth.api.Credentials
import com.neva.javarel.security.auth.api.Guard
import javax.servlet.http.HttpServletRequest

class RequestGuard(request: HttpServletRequest) : Guard {

    override val authenticated: Boolean
        get() = throw UnsupportedOperationException()
    override val authenticable: Authenticable
        get() = throw UnsupportedOperationException()

    override fun authenticate(authenticable: Authenticable) {
        throw UnsupportedOperationException()
    }

    override fun canAuthenticate(credentials: Credentials): Boolean {
        throw UnsupportedOperationException()
    }
}