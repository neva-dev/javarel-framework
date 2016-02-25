package com.neva.javarel.presentation.asset.impl.css

import com.neva.javarel.resource.api.Resource
import com.neva.javarel.presentation.asset.impl.FileAsset
import java.io.InputStream

class CssAsset(resource: Resource) : FileAsset(resource) {

    override fun compile(): InputStream {
        // TODO Implement CSS compilation here

        return super.compile()
    }
}
