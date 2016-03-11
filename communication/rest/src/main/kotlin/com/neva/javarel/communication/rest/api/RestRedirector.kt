package com.neva.javarel.communication.rest.api

import javax.ws.rs.core.Response
import kotlin.reflect.KFunction1

interface RestRedirector {

    fun toAction(action: KFunction1<*, Response>, params: Map<String, Any>): Response

    fun toAction(action: KFunction1<*, Response>): Response

    fun toAction(action: String, params: Map<String, Any>): Response
}