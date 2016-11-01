package com.neva.javarel.presentation.asset.api

import com.neva.javarel.resource.api.Resource
import java.io.InputStream

interface Asset : Resource {

    val mimeType: String

    fun read(): InputStream

    fun compile(): InputStream

}
