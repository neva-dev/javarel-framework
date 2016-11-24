package com.neva.javarel.communication.rest.api

interface RestRoute {

    val method: String

    val path: String

    val action: String

    val names: List<String>

    val className: String

    val methodName: String

    val parameters: List<String>

    fun assemble(params: Map<String, Any>): String

}