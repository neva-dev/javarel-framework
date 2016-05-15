package com.neva.javarel.app.adm.auth

import com.neva.javarel.security.auth.api.Authenticable
import com.neva.javarel.security.auth.api.AuthenticableProvider
import com.neva.javarel.security.auth.api.Guest
import com.neva.javarel.storage.api.DatabaseAdmin
import com.neva.javarel.storage.api.repository.CrudRepository
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service

@Service(UserRepository::class, AuthenticableProvider::class)
@Component
class UserRepository : CrudRepository<User, Long>, AuthenticableProvider {

    @Reference
    private lateinit var db: DatabaseAdmin

    // Authenticable

    override fun byIdentifier(identifier: Any): Authenticable? {
        throw UnsupportedOperationException()
    }

    override fun byCredentials(credentials: Map<String, Any>): Authenticable? {
        throw UnsupportedOperationException()
    }

    override val guest: Authenticable
        get() = byIdentifier(Guest.identifier) ?: Guest()

    // CRUD

    override fun <S : User> save(entity: S): S {
        throw UnsupportedOperationException()
    }

    override fun <S : User> save(entities: Iterable<S>) {
        throw UnsupportedOperationException()
    }

    override fun findOne(id: Long): User {
        throw UnsupportedOperationException()
    }

    override fun findAll(): Iterable<User> {
        throw UnsupportedOperationException()
    }

    override fun findAll(ids: Iterable<Long>) {
        throw UnsupportedOperationException()
    }

    override fun count(): Long {
        throw UnsupportedOperationException()
    }

    override fun delete(entity: User) {
        throw UnsupportedOperationException()
    }

    override fun delete(entities: Iterable<User>) {
        throw UnsupportedOperationException()
    }

    override fun deleteAll() {
        throw UnsupportedOperationException()
    }

    override fun exists(id: Long): Boolean {
        throw UnsupportedOperationException()
    }

}