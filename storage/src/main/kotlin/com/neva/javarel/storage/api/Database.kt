package com.neva.javarel.storage.api

import javax.persistence.EntityManager

/**
 * Represents active database connection
 */
interface Database {

    val connection: Connection

    val connected: Boolean

    fun <R> session(callback: (em: EntityManager) -> R): R

}