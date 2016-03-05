package com.neva.gradle.osgi.container

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.osgi.OsgiPlugin

class ModulePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply(OsgiPlugin)
        project.configurations.create(ContainerConfig.MAIN)

        createConfig(project, ContainerConfig.BUNDLE)
        createConfig(project, ContainerConfig.EMBED)
    }

    def createConfig(Project project, String configName, Closure configurer = {}) {
        def config = project.configurations.create(configName, configurer)

        extendConfig(project, ContainerConfig.COMPILE, { Configuration base ->
            base.extendsFrom(config)
            project.sourceSets.main.compileClasspath += config
            project.sourceSets.test.compileClasspath += config
        })

        return config
    }

    protected extendConfig(Project project, String name, Closure creator) {
        def config = project.configurations.findByName(name)
        if (config != null) {
            creator(config)
        } else {
            project.configurations.whenObjectAdded {
                if (it instanceof Configuration) {
                    config = (Configuration) it
                    if (config.name.equalsIgnoreCase(name)) {
                        creator(config)
                    }
                }
            }
        }
    }
}
