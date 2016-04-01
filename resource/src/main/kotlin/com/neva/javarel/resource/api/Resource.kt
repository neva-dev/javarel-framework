package com.neva.javarel.resource.api

import com.neva.javarel.foundation.adapting.Adaptee
import java.io.InputStream

interface Resource : Adaptee {

    val resolver: ResourceResolver

    val descriptor: ResourceDescriptor

    val inputStream: InputStream

}
