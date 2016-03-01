package com.neva.gradle.osgi.container.builder

import com.neva.gradle.osgi.container.ContainerConfig
import com.neva.gradle.osgi.container.ContainerException
import com.neva.gradle.osgi.container.ContainerExtension
import org.gradle.api.Project

class AbstractBuilder implements ContainerBuilder {

    Project project

    AbstractBuilder(Project project) {
        this.project = project
    }

    @Override
    def main() {
        def config = project.configurations.getByName(ContainerConfig.MAIN.name)
        def size = config.dependencies.size()

        if (size == 0) {
            throw new ContainerException("Configuration named '${ContainerConfig.MAIN.name}' should have container main dependency defined.")
        } else if (size != 1) {
            throw new ContainerException("Configuration named '${ContainerConfig.MAIN.name}' cannot have more than one dependency defined.")
        }

        project.copy {
            from config
            into extension.containerDir
        }
    }

    @Override
    def bundles() {
        def bundleConfig = project.configurations.getByName(ContainerConfig.BUNDLE.name)

        project.copy {
            from bundleConfig
            into "${extension.containerDir}/${extension.bundlePath}"
        }
    }

    @Override
    def configs() {
        // nothing to do
    }

    @Override
    def scripts() {
        // nothing to do
    }

    def ContainerExtension getExtension() {
        project.extensions.getByName(ContainerExtension.NAME) as ContainerExtension
    }
}
