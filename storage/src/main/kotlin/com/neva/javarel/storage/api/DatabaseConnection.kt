package com.neva.javarel.storage.api

import java.util.*
import javax.persistence.spi.PersistenceUnitInfo
import javax.sql.DataSource

/**
 * Represents inactive database connection
 */
interface DatabaseConnection {

    val name: String

    val source: DataSource

    fun configure(info: PersistenceUnitInfo, properties: Properties)

}