package com.neva.javarel.storage.impl

import com.neva.javarel.storage.api.Connection
import javax.sql.DataSource

class GenericConnection(override val name: String, override val source: DataSource) : Connection