package com.neva.javarel.storage.store.api

interface StoreAdmin {

    /**
     * Name of default connection
     */
    val connectionDefault: String

    /**
     * Select default store
     */
    fun store(): Store

    /**
     * Select one of available stores using connection name
     */
    fun store(connectionName: String): Store

    /**
     * Available store connections
     */
    val connections: Set<StoreConnection>

    /**
     * Connected stores
     */
    val connectedStores: Set<Store>

}