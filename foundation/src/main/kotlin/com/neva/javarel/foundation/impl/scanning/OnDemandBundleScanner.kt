package com.neva.javarel.foundation.impl.scanning

import com.neva.javarel.foundation.api.osgi.BundleUtils
import com.neva.javarel.foundation.api.osgi.ClassUtils
import com.neva.javarel.foundation.api.scanning.BundleFilter
import com.neva.javarel.foundation.api.scanning.BundleScanner
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.objectweb.asm.ClassReader
import org.osgi.framework.BundleContext
import org.slf4j.LoggerFactory
import java.io.IOException

@Service(BundleScanner::class)
@Component(immediate = true)
class OnDemandBundleScanner : BundleScanner {

    companion object {
        val LOG = LoggerFactory.getLogger(OnDemandBundleScanner::class.java)
    }

    private var context: BundleContext? = null

    @Activate
    protected fun activate(context: BundleContext) {
        this.context = context
    }

    override fun scan(filter: BundleFilter): Set<Class<*>> {
        val classes = mutableListOf<Class<*>>()

        for (bundle in context!!.bundles) {
            if (!filter.filterBundle(bundle)) {
                continue
            }

            val classUrls = bundle.findEntries("/", "*.class", true)
            if (classUrls != null) {
                classUrls.iterator().forEach { url ->
                    try {
                        val classReader = ClassReader(url.openStream())
                        if (filter.filterClass(bundle, classReader)) {
                            val className = ClassUtils.toClassName(url)
                            classes.add(bundle.loadClass(className))
                        }
                    } catch (e: IOException) {
                        LOG.warn("Class cannot be read: '$url'.")
                    } catch (e: ClassNotFoundException) {
                        LOG.warn("Class not found: '$url'")
                    }
                }
            }
        }

        return classes.toSet()
    }

}