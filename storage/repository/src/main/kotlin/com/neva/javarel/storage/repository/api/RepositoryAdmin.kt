package com.neva.javarel.storage.repository.api

interface RepositoryAdmin {

    /**
     * Select default repository
     */
    fun repository(): Repository

    /**
     * Select one of available repositorys using connection name
     */
    fun repository(connectionName: String): Repository

    /**
     * Available repository connections
     */
    val connections: Set<RepositoryConnection>

    /**
     * Connected repositories
     */
    val connectedRepositories: Set<Repository>

}