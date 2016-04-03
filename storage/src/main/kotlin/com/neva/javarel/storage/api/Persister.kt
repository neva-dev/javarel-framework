package com.neva.javarel.storage.api

import javax.persistence.EntityManagerFactory

interface Persister {

    fun getEntityManagerFactory(persistenceUnitName: String): EntityManagerFactory
}