package com.neva.javarel.communication.rest.impl

import com.google.common.base.Splitter
import com.neva.javarel.communication.rest.api.RestException
import com.neva.javarel.communication.rest.api.RestRoute
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.commons.lang3.StringUtils
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.glassfish.jersey.server.model.Resource
import org.osgi.framework.BundleContext
import java.util.*

@Component(immediate = true)
@Service
class JerseyRestRouter : RestRouter {

    companion object {
        val ALIAS_HEADER_NAME = "Rest-Route-Alias"
        val ALIAS_SPLITTER = Splitter.on("\n").omitEmptyStrings().trimResults().withKeyValueSeparator("=")
        val ALIAS_PART_DELIMITER = "."
    }

    private var components = emptySet<Class<*>>()

    private var allRoutes: Set<RestRoute> = Collections.emptySet()

    override lateinit var aliases: Map<String, String>

    override fun configure(components: Set<Class<*>>) {
        this.components = components
        this.allRoutes = components.fold(mutableSetOf<RestRoute>(), { results, component ->
            val resource = Resource.from(component)
            if (resource != null) {
                resource.childResources.forEach { childResource ->
                    childResource.resourceMethods.forEach { resourceMethod ->
                        results.add(JerseyRestRoute(resource, childResource, resourceMethod))
                    }
                }
            }; results
        })
    }

    override val routes: Set<RestRoute>
        get() = allRoutes

    @Activate
    protected fun start(context: BundleContext) {
        this.aliases = collectAliases(context)
    }

    private fun collectAliases(context: BundleContext): MutableMap<String, String> {
        return context.bundles.fold(mutableMapOf<String, String>(), { aliases, bundle ->
            aliases.putAll(ALIAS_SPLITTER.split(StringUtils.trimToEmpty(bundle.headers.get(ALIAS_HEADER_NAME)))); aliases
        })
    }

    override fun routeByAction(action: String): RestRoute {
        return routeBy({ it.action == expandAlias(action) }, "Route cannot be found by action '$action'")
    }

    override fun routeByName(name: String): RestRoute {
        return routeBy({ it.names.contains(name) }, "Route cannot be found by name '$name'")
    }

    private fun routeBy(predicate: (RestRoute) -> Boolean, notFoundMessage: String): RestRoute {
        return routes.firstOrNull(predicate) ?: throw RestException(notFoundMessage)
    }

    private fun expandAlias(value: String): String {
        val alias = value.substringBefore(ALIAS_PART_DELIMITER)
        val rest = value.substringAfter(ALIAS_PART_DELIMITER)

        if (aliases.contains(alias)) {
            return aliases.get(alias) + ALIAS_PART_DELIMITER + rest
        }

        return value
    }

}