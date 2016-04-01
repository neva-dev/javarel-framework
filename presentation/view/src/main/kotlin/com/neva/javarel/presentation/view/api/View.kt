package com.neva.javarel.presentation.view.api

import com.neva.javarel.resource.api.Resource

interface View : Resource {

    fun render(): String

    fun with(key: String, value: Any): View

    fun with(data: Map<String, Any>): View

}