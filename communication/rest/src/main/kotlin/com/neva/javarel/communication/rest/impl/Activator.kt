package com.neva.javarel.communication.rest.impl

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext

class Activator : BundleActivator {

    override fun start(context: BundleContext?) {
        System.out.println("Starting REST bundle")
    }

    override fun stop(context: BundleContext?) {
        System.out.println("Stopping REST bundle")
    }

}