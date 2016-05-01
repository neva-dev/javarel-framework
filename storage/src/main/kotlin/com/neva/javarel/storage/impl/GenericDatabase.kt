package com.neva.javarel.storage.impl

import com.neva.javarel.storage.api.Connection
import com.neva.javarel.storage.api.Database
import com.neva.javarel.storage.api.DatabaseException
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

class GenericDatabase(override val connection: Connection, val emf: EntityManagerFactory) : Database {

    override val connected: Boolean
        get() = emf.isOpen

    override fun <R> session(callback: (em: EntityManager) -> R): R {
        val em = emf.createEntityManager()

        try {
            em.transaction.begin()
            val result = callback(em)
            em.transaction.commit()
            return result
        } catch (e: Throwable) {
            if (em.transaction.isActive) {
                em.transaction.rollback()
            }

            throw DatabaseException("Database session error.", e)
        }
    }
}