package com.neva.javarel.storage.api

import javax.persistence.EntityManager

/**
 * Represents active database connection
 */
interface Database {

    /**
     * Used connection
     */
    val connection: DatabaseConnection

    /**
     * Check if database connection is estabilished
     */
    val connected: Boolean

    /**
     * Perform operations on database during transaction
     */
    fun <R> session(callback: (em: EntityManager) -> R): R

}