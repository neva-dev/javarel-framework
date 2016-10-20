package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestConfig
import com.neva.javarel.foundation.api.JavarelConstants
import org.apache.felix.scr.annotations.*
import javax.servlet.http.HttpServletRequest

@Component(
        immediate = true,
        metatype = true,
        label = "${JavarelConstants.SERVICE_PREFIX} REST Configuration",
        description = "Configuration for REST components."
)
@Service(RestConfig::class)
class JerseyRestConfig : RestConfig {

    companion object {
        @Property(
                name = FILTER_PATHS_PROP,
                value = "/bin/(.*)",
                label = "Filter paths",
                description = "Determines which paths will not be handled by REST application.",
                unbounded = PropertyUnbounded.ARRAY
        )
        const val FILTER_PATHS_PROP = "filterPathsProp"
    }

    private var props: Map<String, Any>? = null

    private val _filters = mutableMapOf<String, (HttpServletRequest) -> Boolean>()

    @Activate
    private fun activate(props: Map<String, Any>) {
        this.props = props

        filterPaths.forEach { filterPath ->
            registerFilter("filterPath_$filterPath", { request ->
                request.requestURI.matches(Regex(filterPath))
            })
        }
    }

    private val filterPaths: Array<String> by lazy {
        props?.get(FILTER_PATHS_PROP) as Array<String>? ?: arrayOf()
    }

    override val filters: Map<String, (HttpServletRequest) -> Boolean>
        get() = _filters

    override fun registerFilter(name: String, predicate: (HttpServletRequest) -> Boolean) {
        _filters.put(name, predicate)
    }

    override fun unregisterFilter(name: String) {
        _filters.remove(name)
    }

}