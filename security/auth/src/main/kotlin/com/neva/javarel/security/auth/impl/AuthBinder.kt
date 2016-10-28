package com.neva.javarel.security.auth.impl

import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.security.auth.api.Guard
import com.neva.javarel.security.auth.api.Session
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.process.internal.RequestScoped

@Binder
class AuthBinder : AbstractBinder() {

    override fun configure() {
        bind(SessionGuard::class.java).to(Guard::class.java).`in`(RequestScoped::class.java)
        bind(RequestSession::class.java).to(Session::class.java).`in`(RequestScoped::class.java)
    }

}