package com.neva.javarel.storage.repository.api.repository

import com.neva.javarel.storage.repository.api.Repository
import kotlin.reflect.KClass

abstract class DomainRepository<T : Any>(protected val base: Repository, protected val domainClass: KClass<T>) {

    val all: List<T>
        get() = base.dataStore.createQuery(domainClass.java).asList()

}