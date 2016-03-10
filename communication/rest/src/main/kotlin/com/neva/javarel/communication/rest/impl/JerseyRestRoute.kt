package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestRoute
import org.glassfish.jersey.server.model.Resource

class JerseyRestRoute(@Transient val resource: Resource, @Transient val method: Resource) : RestRoute {

    override val methods: Collection<String>
        get() {
            return method.resourceMethods.fold(mutableListOf<String>(), { acc, item ->
                acc.add(item.httpMethod); acc;
            });
        }
    override val uri: String
        get() = "${resource.path}${method.path}"

    override val action: String
        get() = "$className@$methodName"

    val methodName: String
        get() = method.resourceMethods.first().invocable.handlingMethod.name

    val className: String
        get() = method.handlerClasses.first().name


}
