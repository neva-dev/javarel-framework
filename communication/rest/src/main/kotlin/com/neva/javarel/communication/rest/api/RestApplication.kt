package com.neva.javarel.communication.rest.api

interface RestApplication {

    fun getComponents(): Collection<RestComponent>
}