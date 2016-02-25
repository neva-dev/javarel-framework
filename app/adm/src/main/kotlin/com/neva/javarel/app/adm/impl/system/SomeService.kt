package com.neva.javarel.app.adm.impl.system

import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides

@Component
@Instantiate
@Provides
class SomeService {


    fun hello(): String {
        return "hello"
    }

}