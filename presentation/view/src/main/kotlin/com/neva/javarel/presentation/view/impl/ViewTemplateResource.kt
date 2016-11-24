package com.neva.javarel.presentation.view.impl

import com.neva.javarel.resource.api.AdaptableResource
import com.neva.javarel.resource.api.ResourceDescriptor
import com.neva.javarel.resource.api.ResourceResolver
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset

class ViewTemplateResource(val template: String, val extension: String, override val resolver: ResourceResolver) : AdaptableResource() {

    companion object {
        const val CHARSET = "UTF-8"
    }

    override val descriptor: ResourceDescriptor
        get() = ResourceDescriptor("view://template.$extension")
    override val input: InputStream
        get() = ByteArrayInputStream(template.toByteArray(Charset.forName(CHARSET)))

}