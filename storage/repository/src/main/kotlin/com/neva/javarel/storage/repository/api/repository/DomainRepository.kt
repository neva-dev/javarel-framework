package com.neva.javarel.storage.repository.api.repository

import com.neva.javarel.storage.repository.api.Repository
import org.mongodb.morphia.Key
import org.mongodb.morphia.query.Query
import kotlin.reflect.KClass

abstract class DomainRepository<T : Any>(protected val base: Repository, protected val domainClass: KClass<T>) {

    val all: List<T>
        get() = fetchQuery(createQuery())

    fun save(entity: T): Key<T> {
        val result = base.dataStore.save(entity)

        lookup(entity)

        return result
    }

    protected fun createQuery() = base.dataStore.createQuery(domainClass.java)

    protected fun fetchQuery(query: Query<T>): List<T> {
        val entities = query.asList()

        entities.forEach { lookup(it) }

        return entities
    }

    open protected fun lookup(entity: T) {
        // use file storage to post construct entity
    }

}