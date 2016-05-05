package com.neva.javarel.foundation.impl.osgi

import com.neva.javarel.foundation.api.osgi.BundleWatcher
import org.apache.felix.scr.annotations.*
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.osgi.framework.BundleListener

@Service(BundleWatcherDelegate::class)
@Component(immediate = true)
class BundleWatcherDelegate : BundleListener {

    @Reference(referenceInterface = BundleWatcher::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var watchers = mutableSetOf<BundleWatcher>()

    private var context: BundleContext? = null

    @Activate
    protected fun start(context: BundleContext) {
        this.context = context

        context.addBundleListener(this)
    }

    @Deactivate
    protected fun stop() {
        context?.removeBundleListener(this)
    }

    override fun bundleChanged(event: BundleEvent) {
        for (watcher in watchers) {
            watcher.watch(event)
        }
    }

    private fun bindWatchers(watcher: BundleWatcher) {
        watchers.add(watcher)
    }

    private fun unbindWatchers(watcher: BundleWatcher) {
        watchers.remove(watcher)
    }
}