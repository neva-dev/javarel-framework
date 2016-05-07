package com.neva.javarel.foundation.api.scanning

import org.osgi.framework.BundleEvent

interface BundleWatcher {

    /**
     * Perform action on bundle lifecycle event
     */
    fun watch(event: BundleEvent)

}