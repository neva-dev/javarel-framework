package com.neva.javarel.presentation.view.pebble.functions

import com.mitchellbosecke.pebble.extension.Function

abstract class BaseFunction : Function {

    override fun getArgumentNames(): MutableList<String> {
        return mutableListOf()
    }

}