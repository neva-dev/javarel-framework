package com.neva.javarel.foundation.adapting

import kotlin.reflect.KClass

interface Adaptee {

    fun <Target : Any> adaptTo(clazz: KClass<Target>): Target

}