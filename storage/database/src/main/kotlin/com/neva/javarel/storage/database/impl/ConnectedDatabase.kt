package com.neva.javarel.storage.impl

import com.neva.javarel.storage.database.api.Database
import com.neva.javarel.storage.database.api.DatabaseConnection
import com.neva.javarel.storage.database.api.DatabaseException
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceException

class ConnectedDatabase(override val connection: DatabaseConnection, val emf: EntityManagerFactory) : Database {

    override val connected: Boolean
        get() = emf.isOpen

    override fun <R> session(callback: (em: EntityManager) -> R): R {
        val em = try {
            emf.createEntityManager()
        } catch (e: PersistenceException) {
            throw DatabaseException("Database connection error.", e)
        }

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
        } finally {
            em.close()
        }
    }
}