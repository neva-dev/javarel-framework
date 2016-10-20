package com.neva.javarel.communication.rest.api

import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.AbstractBinder as BaseBinder

abstract class AbstractBinder<T> : BaseBinder(), Factory<T> {

    override fun dispose(instance: T) {
        // nothing to do
    }

}