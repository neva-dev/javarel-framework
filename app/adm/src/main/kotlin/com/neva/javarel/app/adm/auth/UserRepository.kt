package com.neva.javarel.app.adm.auth

import com.neva.javarel.storage.api.repository.DomainRepository
import javax.persistence.EntityManager
import kotlin.reflect.KClass

class UserRepository(em: EntityManager) : DomainRepository<User, Long>(em) {

    override val domainClass: KClass<User>
        get() = User::class

}