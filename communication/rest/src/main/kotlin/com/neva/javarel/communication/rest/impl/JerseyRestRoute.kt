package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.Rest
import com.neva.javarel.communication.rest.api.RestRoute
import org.glassfish.jersey.server.model.Resource
import java.lang.reflect.Method

class JerseyRestRoute(@Transient val resource: Resource, @Transient val method: Resource) : RestRoute {

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
        get() = "${resource.path}${method.path}"

    override val action: String
        get() = "$className@$methodName"

    override val methodName: String
        get() = method.resourceMethods.first().invocable.handlingMethod.name

    override val className: String
        get() = method.handlerClasses.first().name

    override val parameters: Collection<String>
        get() {
            return method.resourceMethods.first().invocable.parameters.fold(mutableListOf<String>(), { acc, parameter ->
                acc.add(parameter.sourceName); acc;
            });
        }

    override fun assembleUri(params: Map<String, Any>): String {
        var result = path;
        for ((key, value) in params) {
            //TODO replace path params in using specified params
        }

        return result;
    }

    private val handlingMethod: Method
        get() = method.resourceMethods.first().invocable.handlingMethod

}
