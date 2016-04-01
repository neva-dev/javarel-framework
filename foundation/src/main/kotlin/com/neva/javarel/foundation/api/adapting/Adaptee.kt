package com.neva.javarel.foundation.api.adapting

import kotlin.reflect.KClass

interface Adaptee {

    fun <T : Any> adaptTo(clazz: KClass<T>): T

}