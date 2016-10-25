package com.neva.javarel.storage.database.api

import javax.persistence.EntityManager

interface DatabaseAdmin {

    /**
     * Select default database
     */
    fun database(): Database

    /**
     * Select one of available databases using connection name
     */
    fun database(connectionName: String): Database

    /**
     * Available database connections
     */
    val connections: Set<DatabaseConnection>

    /**
     * Connected databases
     */
    val connectedDatabases: Set<Database>

    /**
     * Perform operations on default database during transaction
     */
    fun <R> session(connectionName: String, callback: (em: EntityManager) -> R): R

    /**
     * Perform operations on default database during transaction
     */
    fun <R> session(callback: (em: EntityManager) -> R): R

}