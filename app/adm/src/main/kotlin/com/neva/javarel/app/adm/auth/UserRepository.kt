package com.neva.javarel.app.adm.auth

import com.neva.javarel.security.auth.api.Authenticable
import com.neva.javarel.security.auth.api.AuthenticableProvider
import com.neva.javarel.security.auth.api.Credentials
import com.neva.javarel.storage.api.repository.DomainRepository
import javax.persistence.EntityManager
import kotlin.reflect.KClass

class UserRepository(em: EntityManager) : DomainRepository<User, Long>(em), AuthenticableProvider {

    override fun byIdentifier(identifier: String): Authenticable? {
        return findBy(mapOf(User.EMAIL_COLUMN to identifier)).firstOrNull()
    }

    override fun byCredentials(credentials: Credentials): Authenticable? {
        return findBy(credentials).firstOrNull()
    }

    override val guest: Authenticable
        get() = byIdentifier(Guest.EMAIL) ?: Guest()

    override val domainClass: KClass<User>
        get() = User::class

}