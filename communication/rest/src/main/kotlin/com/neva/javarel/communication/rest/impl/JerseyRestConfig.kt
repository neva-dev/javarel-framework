package com.neva.javarel.communication.rest.impl

import com.neva.javarel.foundation.JavarelConstants
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service

@Component(immediate = true, metatype = true, label = "${JavarelConstants.servicePrefix} REST Configuration", description = "Configuration for REST components.")
@Service(JerseyRestConfig::class)
class JerseyRestConfig {

    companion object {
        @Property(name = uriPrefixProp, value = "/", label = "URI prefix", description = "Prepends path to resource")
        const val uriPrefixProp = "uriPrefixProp"
    }

    private var props: Map<String, Any>? = null

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props
    }

    val uriPrefix: String by lazy {
        props?.get(uriPrefixProp) as String
    }

}