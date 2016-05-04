package com.neva.javarel.foundation.impl.osgi

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.neva.javarel.foundation.api.osgi.BundleFilter
import com.neva.javarel.foundation.api.osgi.BundleScanner
import com.neva.javarel.foundation.api.osgi.BundleUtils
import org.apache.felix.scr.annotations.*
import org.objectweb.asm.ClassReader
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.osgi.util.tracker.BundleTracker
import org.osgi.util.tracker.BundleTrackerCustomizer
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL

@Service(BundleScanner::class)
@Component(immediate = true)
class GenericBundleScanner : BundleScanner, BundleTrackerCustomizer<Any> {

    companion object {
        val log = LoggerFactory.getLogger(GenericBundleScanner::class.java)
    }

    private var context: BundleContext? = null

    private var tracker: BundleTracker<Any>? = null

    @Reference(referenceInterface = BundleFilter::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var filters = Sets.newConcurrentHashSet<BundleFilter>()

    @Activate
    protected fun start(context: BundleContext) {
        this.context = context

        tracker = BundleTracker(context, Bundle.ACTIVE, this)
        tracker?.open()

        scan()
    }

    @Deactivate
    protected fun stop() {
        tracker?.close()
    }

    override fun scan() {
        for (bundle in context!!.bundles) {
            runFilters(bundle)
        }
    }

    override fun addingBundle(bundle: Bundle, event: BundleEvent?): Any? {
        runFilters(bundle)

        return null
    }

    override fun modifiedBundle(bundle: Bundle, event: BundleEvent?, `object`: Any?) {
        runFilters(bundle)
    }

    override fun removedBundle(bundle: Bundle, event: BundleEvent?, `object`: Any?) {
        runFilters(bundle)
    }

    private fun runFilters(bundle: Bundle) {
        if (!filters.any { it.checkBundle(bundle) }) {
            return
        }

        val urls = collectUrls(bundle)
        for (filter in filters) {
            val loadedClasses = mutableListOf<Class<*>>()
            for (url in urls) {
                try {
                    val classReader = ClassReader(url.openStream())
                    if (filter.checkClass(bundle, classReader)) {
                        val className = BundleUtils.toClassName(url)
                        loadedClasses.add(bundle.loadClass(className))
                    }
                } catch (e: IOException) {
                    log.warn("Class cannot be read: '$url'.")
                } catch (e: ClassNotFoundException) {
                    log.warn("Class not found: '$url'")
                }
            }
            filter.updateClasses(bundle, loadedClasses.toList())
        }
    }

    private fun collectUrls(bundle: Bundle): List<URL> {
        val classNames = Lists.newArrayList<URL>()

        val classUrls = bundle.findEntries("/", "*.class", true)
        if (classUrls != null) {
            while (classUrls.hasMoreElements()) {
                classNames.add(classUrls.nextElement())
            }
        }

        return classNames
    }

    private fun bindFilters(filter: BundleFilter) {
        filters.add(filter)
    }

    private fun unbindFilters(filter: BundleFilter) {
        filters.remove(filter)
    }

}