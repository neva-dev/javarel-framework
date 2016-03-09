package com.neva.javarel.communication.rest.api

interface RestRoute {

    val methods: Collection<String>

    val uri: String

    val action: String

}