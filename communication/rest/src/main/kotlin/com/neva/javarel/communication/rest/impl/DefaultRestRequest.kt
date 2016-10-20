package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestRequest
import com.neva.javarel.foundation.api.lang.MultimapHelper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

// TODO remove it / not usable due to nice JAX-RS API
class DefaultRestRequest(request: HttpServletRequest) : RestRequest, HttpServletRequestWrapper(request) {

    override val parameters: Map<String, Any>
        get() {
            val helper = MultimapHelper(".")

            return parameterNames.toList().fold(mutableMapOf<String, Any>(), { acc, parameterName ->
                helper.put(acc, parameterName, getParameter(parameterName)); acc
            })
        }
}