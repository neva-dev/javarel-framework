package com.neva.javarel.storage.api.repository

import java.io.Serializable

interface CrudRepository<T, ID : Serializable> : Repository<T, ID> {

    fun <S : T> save(entity: S): S

    fun <S : T> save(entities: Iterable<S>)

    fun findOne(id: ID): T

    fun findAll(): Iterable<T>

    fun findAll(ids: Iterable<ID>)

    fun count(): Long

    fun delete(entity: T)

    fun delete(entities: Iterable<T>)

    fun deleteAll()

    fun exists(id: ID): Boolean

}