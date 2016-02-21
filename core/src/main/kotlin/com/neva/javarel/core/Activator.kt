package com.neva.javarel.core

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext

class Activator : BundleActivator {

    override fun start(context: BundleContext?) {
        System.out.println("Starting core bundle")
    }

    override fun stop(context: BundleContext?) {
        System.out.println("Stopping core bundle");
    }

}