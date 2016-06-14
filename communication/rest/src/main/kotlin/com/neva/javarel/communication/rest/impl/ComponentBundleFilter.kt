package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Binder
import com.neva.javarel.foundation.api.scanning.ComponentScanBundleFilter
import javax.ws.rs.ApplicationPath
import javax.ws.rs.Path
import javax.ws.rs.ext.Provider

class ComponentBundleFilter : ComponentScanBundleFilter() {

    override val annotations: Set<Class<out Annotation>>
        get() = setOf(Path::class.java, Provider::class.java, ApplicationPath::class.java, Binder::class.java)

}