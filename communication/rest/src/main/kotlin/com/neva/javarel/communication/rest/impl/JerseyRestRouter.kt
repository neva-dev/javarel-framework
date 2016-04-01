package com.neva.javarel.communication.rest.impl

import com.google.common.base.Splitter
import com.neva.javarel.communication.rest.api.RestComponent
import com.neva.javarel.communication.rest.api.RestException
import com.neva.javarel.communication.rest.api.RestRoute
import com.neva.javarel.communication.rest.api.RestRouter
import org.apache.commons.lang3.StringUtils
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.glassfish.jersey.server.model.Resource
import org.osgi.framework.BundleContext

@Component
@Service
class JerseyRestRouter : RestRouter {

    companion object {
        val aliasHeaderName = "Rest-Route-Alias"
        val aliasSplitter = Splitter.on("\n").omitEmptyStrings().trimResults().withKeyValueSeparator("=")
        val aliasPartDelimiter = "."
    }

    private var components = emptySet<RestComponent>()

    private var aliases = emptyMap<String, String>()

    override fun configure(components: Set<RestComponent>) {
        this.components = components;
    }

    override val routes: Set<RestRoute>
        get() {
            val routes = mutableSetOf<RestRoute>()

            components.forEach { component ->
                val resource = Resource.from(component.javaClass)
                if (resource != null) {
                    resource.childResources.forEach { method ->
                        routes.add(JerseyRestRoute(resource, method))
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
            val bundleAliases = aliasSplitter.split(StringUtils.trimToEmpty(bundle.headers.get(aliasHeaderName)))
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
        val alias = value.substringBefore(aliasPartDelimiter)
        val rest = value.substringAfter(aliasPartDelimiter)

        if (aliases.contains(alias)) {
            return aliases.get(alias) + aliasPartDelimiter + rest
        }

        return value
    }

}