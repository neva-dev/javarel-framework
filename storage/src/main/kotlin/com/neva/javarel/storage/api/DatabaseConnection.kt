package com.neva.javarel.storage.api

import javax.sql.DataSource

/**
 * Represents inactive database connection
 */
interface DatabaseConnection {

    val name: String

    val source: DataSource

}