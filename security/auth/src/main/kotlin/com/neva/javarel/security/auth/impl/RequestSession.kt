package com.neva.javarel.security.auth.impl

import com.neva.javarel.security.auth.api.Session
import javax.servlet.http.HttpSession

class RequestSession(val base: HttpSession) : Session {

    override val id: String
        get() = base.id

    override fun get(name: String): Any? {
        return base.getAttribute(name)
    }

    override fun set(name: String, value: Any) {
        base.setAttribute(name, value)
    }

    override fun remove(name: String) {
        base.removeAttribute(name)
    }

    override fun all(): Map<String, Any> {
        return base.attributeNames.toList().fold(mutableMapOf<String, Any>(), { acc, name ->
            acc.put(name, base.getAttribute(name)); acc
        })
    }

}