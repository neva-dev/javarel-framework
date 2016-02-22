package com.neva.javarel.communication.rest.impl

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext

class Activator : BundleActivator {

    var tracker: HttpTracker? = null

    override fun start(context: BundleContext?) {
        System.out.println("Starting REST bundle")

        tracker = HttpTracker(context)
        tracker?.open()
    }

    override fun stop(context: BundleContext?) {
        System.out.println("Stopping REST bundle")

        tracker?.close()
        tracker = null
    }

}