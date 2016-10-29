package com.neva.javarel.security.auth.impl

import com.neva.javarel.communication.rest.api.AbstractBinder
import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.security.auth.api.Session

@Binder
class AuthBinder : AbstractBinder() {

    override fun configure() {
        bindRequestScoped(SessionGuard::class, Guard::class)
        bindRequestScoped(RequestSession::class, Session::class)
    }

}