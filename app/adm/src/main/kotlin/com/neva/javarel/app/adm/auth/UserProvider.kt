package com.neva.javarel.app.adm.auth

import com.neva.javarel.security.auth.api.Authenticable
import com.neva.javarel.security.auth.api.AuthenticableProvider
import com.neva.javarel.security.auth.api.Credentials
import com.neva.javarel.storage.api.DatabaseAdmin
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

@Service
@Component
class UserProvider : AuthenticableProvider {

    @Reference
    private lateinit var db: DatabaseAdmin

    override fun byIdentifier(identifier: String): User? {
        return db.session { UserRepository(it).findBy(mapOf(User.EMAIL_COLUMN to identifier)).firstOrNull() }
    }

    override fun byCredentials(credentials: Credentials): User? {
        return db.session { UserRepository(it).findBy(credentials).firstOrNull() }
    }

    override val guest: Authenticable
        get() = byIdentifier(Guest.EMAIL) ?: Guest()

}