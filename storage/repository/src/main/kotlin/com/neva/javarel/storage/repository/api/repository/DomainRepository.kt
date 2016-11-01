package com.neva.javarel.storage.repository.api.repository

import com.neva.javarel.storage.repository.api.Repository
import org.mongodb.morphia.Key
import kotlin.reflect.KClass

abstract class DomainRepository<T : Any>(protected val base: Repository, protected val domainClass: KClass<T>) {

    val all: List<T>
        get() {
            val entities = base.dataStore.createQuery(domainClass.java).asList()

            entities.forEach { lookup(it) }

            return entities
        }

    fun save(entity: T): Key<T> {
        val result = base.dataStore.save(entity)

        lookup(entity)

        return result
    }

    open protected fun lookup(entity: T) {
        // load file related data into entities...
    }

}