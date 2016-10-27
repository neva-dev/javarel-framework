package com.neva.javarel.foundation.api.fixture

/**
 * Hook for populating data and configuring application after deploying / only once.
 * Keep in mind that fixtures installing order is not guaranteed (can change in any time, depends on OSGi framework components resolution).
 */
interface Fixture {

    fun install()

    fun uninstall()

}