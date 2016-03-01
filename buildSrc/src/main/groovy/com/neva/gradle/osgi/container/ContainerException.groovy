package com.neva.gradle.osgi.container

import org.gradle.api.GradleException

class ContainerException extends GradleException {

    ContainerException(String message) {
        super(message)
    }

}
