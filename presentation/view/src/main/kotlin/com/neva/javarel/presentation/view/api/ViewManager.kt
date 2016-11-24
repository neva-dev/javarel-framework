package com.neva.javarel.presentation.view.api

import com.neva.javarel.resource.api.Resource

interface ViewManager {

    fun make(resourceUri : String) : View

    fun make(resource : Resource) : View

    fun make(template : String, extension : String) : View

}