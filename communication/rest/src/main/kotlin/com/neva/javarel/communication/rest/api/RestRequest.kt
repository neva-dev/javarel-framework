package com.neva.javarel.communication.rest.api

import javax.servlet.http.HttpServletRequest

interface RestRequest : HttpServletRequest {

    val parameters: Map<String, Any>

}
