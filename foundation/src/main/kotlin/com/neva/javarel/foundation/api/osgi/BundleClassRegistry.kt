package com.neva.javarel.foundation.api.osgi

import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.osgi.util.tracker.BundleTracker
import org.osgi.util.tracker.BundleTrackerCustomizer
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Aggregator for classes with specified annotation
 *
 *
 * When bundle is state is changed (added, removed, modified), then it is executed class scanner which looks for
 * classes with prefixes specified in bundle header
 */
class BundleClassRegistry(private val bundleContext: BundleContext, private val bundleHeader: String,
                          private val annotationClass: Class<out Annotation>) : BundleTrackerCustomizer<Any> {

    companion object {
        private val LOG = LoggerFactory.getLogger(BundleClassRegistry::class.java)
    }

    private val tracker: BundleTracker<Any>

    private val classes = ConcurrentHashMap<Long, List<Class<*>>>()

    init {
        tracker = BundleTracker(bundleContext, Bundle.RESOLVED or Bundle.ACTIVE, this)
    }

    override fun addingBundle(bundle: Bundle, event: BundleEvent?): Any? {
        val scanned = BundleClassScanner(bundle).findClasses(bundleHeader, annotationClass)

        if (scanned.size > 0) {
            if (LOG.isDebugEnabled) {
                LOG.debug("Adding classes ({}) from bundle: {}", scanned.size, bundle.symbolicName)
            }

            classes.put(bundle.bundleId, scanned)
        }

        return null
    }

    override fun modifiedBundle(bundle: Bundle, event: BundleEvent?, `object`: Any?) {
        val scanned = BundleClassScanner(bundle).findClasses(bundleHeader, annotationClass)

        classes.remove(bundle.bundleId)
        if (scanned.size > 0) {
            if (LOG.isDebugEnabled) {
                LOG.debug("Updating classes ({}) from bundle: {}", scanned.size, bundle.symbolicName)
            }

            classes.put(bundle.bundleId, scanned)
        }
    }

    override fun removedBundle(bundle: Bundle, event: BundleEvent?, `object`: Any?) {
        val registered = classes[bundle.bundleId]!!

        if (LOG.isDebugEnabled) {
            LOG.debug("Removing classes ({}) from bundle: {}", registered.size, bundle.symbolicName)
        }

        classes.remove(bundle.bundleId)
    }

    @Synchronized fun getClasses(): List<Class<*>> {
        val flattened = ArrayList<Class<*>>()
        for (entry in classes.entries) {
            flattened.addAll(entry.value)
        }

        return flattened
    }

    fun open() {
        tracker.open()
    }

    fun close() {
        tracker.close()
    }
}