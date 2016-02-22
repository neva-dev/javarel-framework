package com.neva.javarel.communication.rest.impl

import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference
import org.osgi.service.http.HttpService
import org.osgi.util.tracker.ServiceTracker

class HttpTracker(bundleContext: BundleContext?) : ServiceTracker<HttpService, Unit>(bundleContext, HttpService::class.java, null) {

    override fun addingService(reference: ServiceReference<HttpService>?) {
        super.addingService(reference)
    }

    override fun removedService(reference: ServiceReference<HttpService>?, service: Unit?) {
        super.removedService(reference, service)
    }
}