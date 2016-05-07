package com.neva.javarel.foundation.api.scanning

interface BundleScanner {

    fun scan(filter: BundleFilter): Set<Class<*>>

}