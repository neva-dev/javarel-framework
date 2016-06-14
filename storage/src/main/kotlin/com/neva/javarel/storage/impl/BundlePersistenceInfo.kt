package com.neva.javarel.storage.impl

import org.apache.openjpa.lib.util.MultiClassLoader
import org.apache.openjpa.persistence.PersistenceUnitInfoImpl
import org.osgi.framework.BundleContext
import org.osgi.framework.wiring.BundleWiring

class BundlePersistenceInfo(val context: BundleContext) : PersistenceUnitInfoImpl() {

    override fun getClassLoader(): ClassLoader {
        val cl = MultiClassLoader()

        context.bundles.forEach { bundle ->
            val wiring = bundle.adapt(BundleWiring::class.java)
            if (wiring != null) {
                cl.addClassLoader(wiring.getClassLoader())

            }
        }

        return cl
    }
}