package com.neva.javarel.security.auth.api

interface Session {

    val id: String

    fun get(name: String) : Any?

    fun set(name: String, value: Any)

    fun remove(name: String)

    fun all(): Map<String, Any>

}