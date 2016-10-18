package com.neva.javarel.foundation.impl.scanning

import com.neva.javarel.foundation.api.scanning.BundleWatcher
import org.apache.felix.scr.annotations.*
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.osgi.framework.BundleListener

@Component(immediate = true)
@Service(BundleWatcherDelegate::class)
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