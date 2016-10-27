package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestRoute
import com.neva.javarel.communication.rest.api.Route
import org.apache.commons.lang3.StringUtils
import org.glassfish.jersey.server.model.Parameter
import org.glassfish.jersey.server.model.Resource
import org.glassfish.jersey.server.model.ResourceMethod

class JerseyRestRoute(
        @Transient private val resource: Resource,
        @Transient private val methodResource: Resource,
        @Transient private val methodObj: ResourceMethod
) : RestRoute {

    companion object {
        val PATH_SEPARATOR = "/"
        val PARAM_TYPE_DELIMITER = ":"
        val PARAM_TOKEN_START = "{"
        val PARAM_TOKEN_END = "}"

        fun mergePath(vararg parts: String): String {
            val normalized = parts.fold(mutableListOf<String>(), { result, part ->
                val path = part.removePrefix(PATH_SEPARATOR).removeSuffix(PATH_SEPARATOR)
                if (path.isNotBlank()) {
                    result.add(path)
                }
                result
            })

            return PATH_SEPARATOR + normalized.joinToString(PATH_SEPARATOR)
        }
    }

    override val name: String?
        get() {
            var name: String? = null
            val routeAnnotation = methodObj.invocable.handlingMethod.declaredAnnotations.find { it is Route }
            if (routeAnnotation != null) {
                name = (routeAnnotation as Route).name
            }

            return name
        }

    override val method: String
        get() {
            return methodObj.httpMethod
        }

    override val path: String
        get() = mergePath(resource.path, methodResource.path)

    override val action: String
        get() = "$className.$methodName"

    override val methodName: String
        get() = methodObj.invocable.handlingMethod.name

    override val className: String
        get() = methodResource.handlerClasses.first().name

    override val parameters: List<String>
        get() {
            return methodObj.invocable.parameters.fold(mutableListOf<String>(), { result, parameter ->
                when (parameter) {
                    is Parameter.BeanParameter -> parameter.parameters.forEach { if (it.sourceName != null) result.add(it.sourceName) }
                    else -> if (parameter.sourceName != null) result.add(parameter.sourceName)
                }; result
            })
        }

    override fun assemble(params: Map<String, Any>): String {
        var result = path
        for ((key, value) in params) {
            StringUtils.substringsBetween(path, PARAM_TOKEN_START, PARAM_TOKEN_END).forEach { variable ->
                val name = variable.split(PARAM_TYPE_DELIMITER)[0]
                if (name == key) {
                    result = StringUtils.replace(result, PARAM_TOKEN_START + variable + PARAM_TOKEN_END, value as String)
                }
            }
        }

        return result
    }

    override fun toString(): String {
        return "REST Route (path=$path, method=$method, action=$action, name=$name)"
    }

}
