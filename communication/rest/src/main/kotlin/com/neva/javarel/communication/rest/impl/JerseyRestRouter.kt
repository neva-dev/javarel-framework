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

@Component(immediate = true)
@Service
class JerseyRestRouter : RestRouter {

    companion object {
        val ALIAS_HEADER_NAME = "Rest-Route-Alias"
        val ALIAS_SPLITTER = Splitter.on("\n").omitEmptyStrings().trimResults().withKeyValueSeparator("=")
        val ALIAS_PART_DELIMITER = "."
    }

    private var components = emptySet<Class<*>>()

    override lateinit var aliases: Map<String, String>

    override fun configure(components: Set<Class<*>>) {
        this.components = components
    }

    override val routes: Set<RestRoute>
        get() {
            val routes = mutableSetOf<RestRoute>()

            components.forEach { component ->
                val resource = Resource.from(component)
                if (resource != null) {
                    resource.childResources.forEach { childResource ->
                        childResource.resourceMethods.forEach { resourceMethod ->
                            routes.add(JerseyRestRoute(resource, childResource, resourceMethod))
                        }
                    }
                }
            }

            return routes
        }

    @Activate
    protected fun start(context: BundleContext) {
        this.aliases = collectAliases(context)
    }

    private fun collectAliases(context: BundleContext): MutableMap<String, String> {
        val result = mutableMapOf<String, String>()
        for (bundle in context.bundles) {
            val bundleAliases = ALIAS_SPLITTER.split(StringUtils.trimToEmpty(bundle.headers.get(ALIAS_HEADER_NAME)))
            result.putAll(bundleAliases)
        }

        return result
    }

    override fun routeByAction(action: String): RestRoute {
        return routeBy({ it.action == expandAlias(action) }, "Route cannot be found by action '$action'")
    }

    override fun routeByName(name: String): RestRoute {
        return routeBy({ it.name == expandAlias(name) }, "Route cannot be found by name '$name'")
    }

    private fun routeBy(predicate: (RestRoute) -> Boolean, notFoundMessage: String): RestRoute {
        routes.forEach { route ->
            if (predicate(route)) {
                return route;
            }
        }

        throw RestException(notFoundMessage)
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