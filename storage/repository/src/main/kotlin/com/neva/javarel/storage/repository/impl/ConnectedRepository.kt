package com.neva.javarel.storage.repository.impl

import com.neva.javarel.storage.repository.api.Repository
import com.neva.javarel.storage.repository.api.RepositoryConnection
import org.mongodb.morphia.Datastore

class ConnectedRepository(override val connection: RepositoryConnection, override val dataStore: Datastore) : Repository