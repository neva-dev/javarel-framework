package com.neva.javarel.communication.rest.api

interface RestApplication {

    fun getResources(): Collection<RestResource>
}