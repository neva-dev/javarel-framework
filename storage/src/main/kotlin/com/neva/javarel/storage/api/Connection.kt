package com.neva.javarel.storage.api

import javax.sql.DataSource

/**
 * Represents inactive database connection
 */
interface Connection {

    val name: String

    val source: DataSource

}