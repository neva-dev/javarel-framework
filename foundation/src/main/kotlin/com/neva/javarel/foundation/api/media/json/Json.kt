package com.neva.javarel.foundation.api.media.json

import com.google.gson.GsonBuilder
import kotlin.reflect.KClass

object Json {

    val gson = GsonBuilder().serializeNulls().create()

    fun serialize(obj: Any): String {
        return gson.toJson(obj)
    }

    fun <T : Any> unserialize(json: String, clazz: KClass<T>): T {
        return gson.fromJson(json, clazz.java)
    }

}