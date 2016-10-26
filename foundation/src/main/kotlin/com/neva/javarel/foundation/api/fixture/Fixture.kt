package com.neva.javarel.foundation.api.fixture

/**
 * Hook for populating data and configuring application only once after deploying.
 */
interface Fixture {

    fun order(): Int

    fun install()

    fun uninstall()

}