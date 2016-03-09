package com.neva.javarel.communication.rest.api

interface RestApplication {

    val components: Set<RestComponent>

    fun update()
}