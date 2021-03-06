package com.neva.javarel.storage.database.api.repository

import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.NonUniqueResultException
import javax.persistence.TypedQuery
import javax.persistence.criteria.*
import kotlin.reflect.KClass

abstract class DomainRepository<T : Any, ID : Serializable>(protected val em: EntityManager, protected val flushOnChange: Boolean = true) : CrudRepository<T, ID> {

    protected abstract val domainClass: KClass<T>

    // TODO read column name (@Column) annotated with @Id
    protected val idAttribute: String
        get() = "id"

    override fun <S : T> save(entity: S): S {
        em.persist(entity)
        flush()

        return entity
    }

    override fun <S : T> save(entities: Iterable<S>): Iterable<S> {
        val results: MutableList<S> = mutableListOf()

        for (entity in entities) {
            results.add(save(entity))
        }
        flush()

        return results
    }

    override fun findOne(id: ID): T? {
        return em.find(domainClass.java, id)
    }

    override fun findOneBy(props: Map<String, Any>, operator: Predicate.BooleanOperator): T? {
        try {
            return findByQuery(props, operator).singleResult
        } catch (e: NoResultException) {
            return null
        } catch (e: NonUniqueResultException) {
            return null
        }
    }

    override fun findBy(props: Map<String, Any>, operator: Predicate.BooleanOperator): Iterable<T> {
        return findByQuery(props, operator).resultList
    }

    private fun findByQuery(props: Map<String, Any>, operator: Predicate.BooleanOperator): TypedQuery<T> {
        val query = createQuery({ builder, criteria, root ->
            val predicates = props.entries.fold(mutableListOf<Predicate>(), { acc, entry ->
                acc.add(builder.equal(root.get<Any>(entry.key), entry.value)); acc
            }).toTypedArray()
            val predicate = when (operator) {
                Predicate.BooleanOperator.AND -> builder.and(*predicates)
                Predicate.BooleanOperator.OR -> builder.or(*predicates)
            }

            criteria.where(predicate)

            return@createQuery em.createQuery(criteria)
        })
        return query
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
        flush()
    }

    override fun delete(entities: Iterable<T>) {
        for (entity in entities) {
            delete(entity)
        }
        flush()
    }

    override fun deleteAll() {
        for (entity in findAll()) {
            delete(entity)
        }
        flush()
    }

    override fun exists(id: ID): Boolean {
        return findOne(id) != null
    }

    protected fun flush() {
        if (flushOnChange) {
            em.flush()
        }
    }

    protected fun createQuery(): TypedQuery<T> {
        return createQuery({ builder, criteria, root -> em.createQuery(criteria) })
    }

    protected fun createQuery(configurer: ((builder: CriteriaBuilder, criteria: CriteriaQuery<T>, root: Root<T>) -> TypedQuery<T>)): TypedQuery<T> {
        val builder = em.criteriaBuilder
        val criteria = builder.createQuery(domainClass.java)
        val root = criteria.from(domainClass.java)

        criteria.select(root)

        return configurer(builder, criteria, root)
    }

}