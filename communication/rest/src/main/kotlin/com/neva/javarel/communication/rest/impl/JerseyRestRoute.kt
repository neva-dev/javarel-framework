package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Rest
import com.neva.javarel.communication.rest.api.RestRoute
import org.apache.commons.lang3.StringUtils
import org.glassfish.jersey.server.model.Resource
import java.lang.reflect.Method

class JerseyRestRoute(@Transient val resource: Resource, @Transient val method: Resource) : RestRoute {

    companion object {
        val pathSeparator = "/"
        val paramTypeDelimiter = ":"
        val paramStart = "{"
        val paramEnd = "}"
    }

    override val name: String?
        get() {
            var name: String? = null;
            val ann = handlingMethod.declaredAnnotations.find { it is Rest }
            if (ann != null) {
                name = (ann as Rest).name
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
            StringUtils.substringsBetween(path, paramStart, paramEnd).forEach { variable ->
                val name = variable.split(paramTypeDelimiter)[0]
                if (name == key) {
                    result = StringUtils.replace(result, paramStart + variable + paramEnd, value as String)
                }
            }
        }

        return result;
    }

    private val handlingMethod: Method
        get() = method.resourceMethods.first().invocable.handlingMethod

    private fun mergePath(vararg parts: String): String {
        val normalized = parts.fold(mutableListOf<String>(), { result, part ->
            val path = part.removePrefix(pathSeparator).removeSuffix(pathSeparator)
            if (path.isNotBlank()) {
                result.add(path);
            }
            result;
        })

        return pathSeparator + normalized.joinToString(pathSeparator)
    }

}
