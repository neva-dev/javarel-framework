package com.neva.javarel.communication.rest.api

import java.net.URI
import javax.ws.rs.core.Response

object Redirect {

    fun to(uri: String): Response? {
        return Response.seeOther(URI(uri)).build()
    }

}