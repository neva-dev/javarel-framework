package com.neva.javarel.communication.rest.impl

import com.neva.javarel.foundation.api.JavarelConstants
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service

/**
 * TODO remove it, we are attaching at root
 */
@Component(immediate = true, metatype = true, label = "${JavarelConstants.servicePrefix} REST Configuration", description = "Configuration for REST components.")
@Service(JerseyRestConfig::class)
class JerseyRestConfig {

    companion object {
        @Property(name = URI_PREFIX_PROP, value = "/", label = "URI prefix", description = "Prepends path to resource")
        const val URI_PREFIX_PROP = "uriPrefixProp"
    }

    private var props: Map<String, Any>? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    val uriPrefix: String by lazy {
        props?.get(URI_PREFIX_PROP) as String
    }

}