package com.neva.javarel.framework.api.structure

interface Module {

    val priority : Int

    val name : String

    val options: Map<String, Any>

}