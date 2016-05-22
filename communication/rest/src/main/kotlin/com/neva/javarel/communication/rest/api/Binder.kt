package com.neva.javarel.communication.rest.api

import javax.inject.Qualifier

/**
 * Register new binder
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Binder
