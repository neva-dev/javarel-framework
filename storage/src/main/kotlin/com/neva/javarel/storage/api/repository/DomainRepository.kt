package com.neva.javarel.storage.api.repository

import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root
import kotlin.reflect.KClass

abstract class DomainRepository<T : Any, ID : Serializable>(val em: EntityManager) : CrudRepository<T, ID> {

    protected abstract val domainClass: KClass<T>

    // TODO read column name (@Column) annotated with @Id
    protected val idAttribute: String
        get() = "id"

    override fun <S : T> save(entity: S): S {
        em.persist(entity);

        return entity;
    }

    override fun <S : T> save(entities: Iterable<S>): Iterable<S> {
        val results: MutableList<S> = mutableListOf()

        for (entity in entities) {
            results.add(save(entity))
        }

        return results
    }

    override fun findOne(id: ID): T? {
        return em.find(domainClass.java, id)
    }


    override fun findBy(props: Map<String, Any>): Iterable<T> {
        return createQuery({ builder, criteria, root ->
            for ((name, value) in props) {
                criteria.where(builder.equal(root.get<Any>(name), value));
            }

            return@createQuery em.createQuery(criteria)
        }).resultList
    }

    override fun findAll(): Iterable<T> {
        return createQuery().resultList
    }

    override fun findAll(ids: Iterable<ID>): Iterable<T> {
        return createQuery({ builder, criteria, root ->
            val path: Path<ID> = root.get(idAttribute)
            val param = builder.parameter(Iterable::class.java)

            criteria.where(path.`in`(param))

            val query = em.createQuery(criteria)
            query.setParameter(param, ids)

            return@createQuery query
        }).resultList
    }

    /**
     * TODO optimize by query as in Spring Data
     */
    override fun count(): Long {
        return findAll().toList().size.toLong()
    }

    override fun delete(entity: T) {
        em.remove(if (em.contains(entity)) entity else em.merge(entity));
    }

    override fun delete(entities: Iterable<T>) {
        for (entity in entities) {
            delete(entity)
        }
    }

    override fun deleteAll() {
        for (entity in findAll()) {
            delete(entity)
        }
    }

    override fun exists(id: ID): Boolean {
        return findOne(id) != null
    }

    protected fun createQuery(): TypedQuery<T> {
        return createQuery ({ builder, criteria, root -> em.createQuery(criteria) })
    }

    protected fun createQuery(configurer: ((builder: CriteriaBuilder, criteria: CriteriaQuery<T>, root: Root<T>) -> TypedQuery<T>)): TypedQuery<T> {
        val builder = em.getCriteriaBuilder();
        val criteria = builder.createQuery(domainClass.java);
        val root = criteria.from(domainClass.java);

        criteria.select(root)

        return configurer(builder, criteria, root)
    }

}