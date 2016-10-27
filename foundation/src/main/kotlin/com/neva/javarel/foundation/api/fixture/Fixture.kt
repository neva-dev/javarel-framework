package com.neva.javarel.foundation.api.fixture

/**
 * Hook for populating data and configuring application only once after deploying.
 */
interface Fixture {

    val order: Int

    fun install()

    fun uninstall()

}