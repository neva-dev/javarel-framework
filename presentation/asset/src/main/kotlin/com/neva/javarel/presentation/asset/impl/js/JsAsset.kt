package com.neva.javarel.presentation.asset.impl.js

import com.neva.javarel.resource.api.Resource
import com.neva.javarel.presentation.asset.impl.FileAsset
import java.io.InputStream

class JsAsset(resource: Resource) : FileAsset(resource) {

    override fun compile(): InputStream {
        // TODO Implement JS compilation here

        return super.compile()
    }
}
