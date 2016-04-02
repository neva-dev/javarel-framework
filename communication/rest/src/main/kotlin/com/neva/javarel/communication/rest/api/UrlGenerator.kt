package com.neva.javarel.communication.rest.api

import javax.ws.rs.core.Response
import kotlin.reflect.KFunction1

interface UrlGenerator {

    fun action(action: KFunction1<*, Response>, params: Map<String, Any>): String

    fun action(action: KFunction1<*, Response>): String

    fun action(action: String, params: Map<String, Any>): String

    fun action(action: String): String

    fun name(name: String, params: Map<String, Any>): String

    fun name(name: String): String

    fun assemble(route: RestRoute, params: Map<String, Any>): String
}