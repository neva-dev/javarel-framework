package com.neva.javarel.storage.repository.impl

import com.mongodb.DBObject
import org.mongodb.morphia.annotations.ConstructorArgs
import org.mongodb.morphia.mapping.DefaultCreator
import org.mongodb.morphia.mapping.MappedField
import org.mongodb.morphia.mapping.Mapper

/**
 * Use pre-loaded entity classes while creating objects. Do not use class loader.
 */
class EntityObjectFactory(val classes: Set<Class<*>>) : DefaultCreator() {

    override fun <T : Any?> createInstance(clazz: Class<T>?, dbObj: DBObject): T {
        var c: Class<T>? = getClass(dbObj)
        if (c == null) {
            c = clazz
        }

        return createInstance(c)
    }

    override fun createInstance(mapper: Mapper, mf: MappedField, dbObj: DBObject): Any {
        var c: Class<*>? = getClass<Any>(dbObj)

        if (c == null) {
            c = if (mf.isSingleValue) mf.concreteType else mf.subClass
        }

        try {
            return createInstance(c, dbObj)
        } catch (e: RuntimeException) {
            val argAnn = mf.getAnnotation(ConstructorArgs::class.java) ?: throw e
            val args = arrayOfNulls<Any>(argAnn.value.size)

            val argTypes = arrayOfNulls<Class<*>>(argAnn.value.size)
            for (i in 0..argAnn.value.size - 1) {
                val `val` = dbObj.get(argAnn.value[i])
                args[i] = `val`
                argTypes[i] = `val`.javaClass
            }
            try {
                val constructor = c!!.getDeclaredConstructor(*argTypes)
                constructor.isAccessible = true
                return constructor.newInstance(*args)
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getClass(dbObj: DBObject): Class<T>? {
        var result: Class<T>? = null
        if (dbObj.containsField(Mapper.CLASS_NAME_FIELDNAME)) {
            val className = dbObj.get(Mapper.CLASS_NAME_FIELDNAME) as String
            result = classes.find { it.name == className } as Class<T>

        }

        return result
    }
}