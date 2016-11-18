package com.neva.javarel.foundation.api.lang

import java.lang.reflect.Field

object ReflectionUtils {

    /**
     * @link http://stackoverflow.com/a/16966699
     */
    fun getInheritedFields(clazz: Class<*>, exclusiveParent: Class<*>? = null): List<Field> {
        val currentClassFields: MutableList<Field> = mutableListOf()
        currentClassFields.addAll(clazz.declaredFields)

        val parentClass = clazz.superclass

        if (parentClass != null && (exclusiveParent == null || parentClass != exclusiveParent)) {
            val parentClassFields = getInheritedFields(parentClass, exclusiveParent)
            currentClassFields.addAll(parentClassFields)
        }

        return currentClassFields
    }

}