package com.neva.javarel.storage.repository.impl

import org.mongodb.morphia.mapping.DefaultCreator
import org.osgi.framework.BundleContext
import org.osgi.framework.wiring.BundleWiring

class BundleObjectFactory(private val bundleContext: BundleContext) : DefaultCreator() {

    override fun getClassLoaderForClass(): ClassLoader {
        return (bundleContext.bundle.adapt(BundleWiring::class.java) as BundleWiring).classLoader
    }

}