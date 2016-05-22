package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Route
import com.neva.javarel.communication.rest.api.RestRoute
import org.apache.commons.lang3.StringUtils
import org.glassfish.jersey.server.model.Resource
import java.lang.reflect.Method

class JerseyRestRoute(@Transient val resource: Resource, @Transient val method: Resource) : RestRoute {

    companion object {
        val PATH_SEPARATOR = "/"
        val PARAM_TYPE_DELIMITER = ":"
        val PARAM_TOKEN_START = "{"
        val PARAM_TOKEN_END = "}"

        fun mergePath(vararg parts: String): String {
            val normalized = parts.fold(mutableListOf<String>(), { result, part ->
                val path = part.removePrefix(PATH_SEPARATOR).removeSuffix(PATH_SEPARATOR)
                if (path.isNotBlank()) {
                    result.add(path);
                }
                result;
            })

            return PATH_SEPARATOR + normalized.joinToString(PATH_SEPARATOR)
        }
    }

    override val name: String?
        get() {
            var name: String? = null;
            val routeAnnotation = handlingMethod.declaredAnnotations.find { it is Route }
            if (routeAnnotation != null) {
                name = (routeAnnotation as Route).name
            }

            return name
        }

    override val methods: Collection<String>
        get() {
            return method.resourceMethods.fold(mutableListOf<String>(), { acc, item ->
                acc.add(item.httpMethod); acc;
            });
        }
    override val path: String
        get() = mergePath(resource.path, method.path)

    override val action: String
        get() = "$className.$methodName"

    override val methodName: String
        get() = method.resourceMethods.first().invocable.handlingMethod.name

    override val className: String
        get() = method.handlerClasses.first().name

    override val parameters: Collection<String>
        get() {
            return method.resourceMethods.first().invocable.parameters.fold(mutableListOf<String>(), { result, parameter ->
                result.add(parameter.sourceName); result;
            });
        }

    override fun assembleUri(params: Map<String, Any>): String {
        var result = path;
        for ((key, value) in params) {
            StringUtils.substringsBetween(path, PARAM_TOKEN_START, PARAM_TOKEN_END).forEach { variable ->
                val name = variable.split(PARAM_TYPE_DELIMITER)[0]
                if (name == key) {
                    result = StringUtils.replace(result, PARAM_TOKEN_START + variable + PARAM_TOKEN_END, value as String)
                }
            }
        }

        return result;
    }

    private val handlingMethod: Method
        get() = method.resourceMethods.first().invocable.handlingMethod

    override fun toString(): String {
        return "REST Route (path=$path, methods=$methods, action=$action, name=$name)"
    }

}
