package com.neva.javarel.storage.store.api.store

import com.neva.javarel.storage.store.api.Store
import org.bson.types.ObjectId
import org.mongodb.morphia.Key
import org.mongodb.morphia.query.Query
import kotlin.reflect.KClass

abstract class DomainStore<T : Any>(protected val base: Store, protected val domainClass: KClass<T>) {

    val all: List<T>
        get() = fetchQuery(createQuery())

    fun save(entity: T): Key<T> {
        val result = base.dataStore.save(entity)

        init(entity)

        return result
    }

    protected fun createQuery() = base.dataStore.createQuery(domainClass.java)

    protected fun fetchQuery(query: Query<T>): List<T> {
        val entities = query.asList()

        entities.forEach { init(it) }

        return entities
    }

    open protected fun init(entity: T) {
        // use file storage to post construct entity
    }

    fun find(id: String): T? {
        val entity = base.dataStore.get<T, ObjectId>(domainClass.java, ObjectId(id))
        if (entity != null) {
            init(entity)
        }

        return entity
    }

    fun delete(entity: T) {
        base.dataStore.delete(entity)
    }

}