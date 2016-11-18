package com.neva.javarel.foundation.api.injection

/**
 * Annotation should be used to mark elements for which custom injection for OSGi services is available.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class Osgi