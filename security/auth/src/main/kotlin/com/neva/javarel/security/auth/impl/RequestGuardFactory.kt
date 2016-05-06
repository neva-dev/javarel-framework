package com.neva.javarel.security.auth.impl

import com.neva.javarel.security.auth.api.Guard
import org.glassfish.hk2.api.Factory
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

class RequestGuardFactory : Factory<Guard> {

    @Inject
    private lateinit var request: HttpServletRequest

    @Override
    override fun provide(): Guard {
        return RequestGuard(request)
    }

    @Override
    override fun dispose(t: Guard) {
        // nothing to do
    }
}