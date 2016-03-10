package com.neva.javarel.communication.rest.api

import javax.inject.Qualifier

/**
 * Allows REST routes to be named
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Rest(val name: String)
