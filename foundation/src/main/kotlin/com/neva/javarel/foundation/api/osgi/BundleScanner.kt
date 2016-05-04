package com.neva.javarel.foundation.api.osgi

interface BundleScanner {

    fun scan(filter: BundleFilter): Collection<Class<*>>

}