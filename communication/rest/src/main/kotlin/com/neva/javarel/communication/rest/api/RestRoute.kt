package com.neva.javarel.communication.rest.api

interface RestRoute {

    val methods: Collection<String>

    val path: String

    val action: String

    val name: String?

    val className: String

    val methodName: String

    val parameters: Collection<String>

    fun assembleUri(params: Map<String, Any>): String

}