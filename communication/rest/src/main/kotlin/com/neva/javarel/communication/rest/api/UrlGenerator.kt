package com.neva.javarel.communication.rest.api

import kotlin.reflect.KFunction1

interface UrlGenerator {

    fun action(action: KFunction1<*, *>, params: Map<String, Any>): String

    fun action(action: KFunction1<*, *>, params: List<Any>): String

    fun action(action: KFunction1<*, *>): String

    fun action(action: String, params: Map<String, Any>): String

    fun action(action: String, params: List<Any>): String

    fun action(action: String): String

    fun name(name: String, params: Map<String, Any>): String

    fun name(name: String, params: List<Any>): String

    fun name(name: String): String

}