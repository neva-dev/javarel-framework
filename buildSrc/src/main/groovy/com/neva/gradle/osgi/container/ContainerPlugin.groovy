package com.neva.gradle.osgi.container

import org.gradle.api.Plugin
import org.gradle.api.Project

class ContainerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "OSGI container plugin applied!"
    }
}
