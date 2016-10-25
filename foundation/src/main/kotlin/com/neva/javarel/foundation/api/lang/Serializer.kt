package com.neva.javarel.foundation.api.lang

import java.io.*
import java.util.Base64

object Serializer {

    fun unserialize(str: String): Any {
        val data = Base64.getDecoder().decode(str)
        val ois = ObjectInputStream(
                ByteArrayInputStream(data))
        val o = ois.readObject()

        ois.close()

        return o
    }

    fun serialize(obj: Serializable): String {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)

        oos.writeObject(obj)
        oos.close()

        return Base64.getEncoder().encodeToString(baos.toByteArray())
    }
}
