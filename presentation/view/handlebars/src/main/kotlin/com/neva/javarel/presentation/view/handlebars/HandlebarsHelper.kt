package com.neva.javarel.presentation.view.handlebars

import com.github.jknack.handlebars.Helper

interface HandlebarsHelper<T> : Helper<T> {

    val name : String

}