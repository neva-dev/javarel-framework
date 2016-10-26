package com.neva.javarel.security.auth.impl

import com.neva.javarel.communication.rest.api.AbstractBinder
import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.communication.rest.api.Uses
import com.neva.javarel.security.auth.api.Auth
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.security.auth.api.Session

@Binder
class SessionGuardBinder : AbstractBinder<Guard>() {

    @Uses
    private lateinit var auth: Auth

    @Uses
    private lateinit var session: Session

    override fun configure() {
        bindPerRequest(Guard::class)
    }

    override fun provide(): Guard {
        return SessionGuard(session, auth)
    }

}