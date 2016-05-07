package com.neva.javarel.foundation.api.osgi

import org.osgi.framework.Bundle
import org.osgi.framework.Constants

object BundleUtils {

    fun isFragment(bundle: Bundle): Boolean {
        return bundle.headers.get(Constants.FRAGMENT_HOST) != null
    }

    fun getHostBundle(fragment: Bundle): Bundle? {
        for (host in fragment.bundleContext.bundles) {
            if (host.symbolicName == fragment.headers.get(Constants.FRAGMENT_HOST)) {
                return host
            }
        }

        return null
    }

}