package com.neva.javarel.storage.impl.connection

import com.neva.javarel.storage.api.DatabaseConnection
import java.util.*
import javax.persistence.spi.PersistenceUnitInfo

abstract class AbstractDatabaseConnection : DatabaseConnection {

    override fun configure(info: PersistenceUnitInfo, properties: Properties) {
        // nothing to do
    }
}