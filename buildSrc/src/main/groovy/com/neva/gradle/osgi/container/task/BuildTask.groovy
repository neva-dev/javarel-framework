package com.neva.gradle.osgi.container.task

import com.neva.gradle.osgi.container.ContainerException
import org.gradle.api.tasks.TaskAction

class BuildTask extends ContainerTask {

    static final NAME = "createOsgiContainer"

    BuildTask() {
        inputs.files(extension.runners)
        outputs.dir(extension.containerDir)

        inputs.properties(extension.config)
        inputs.property("exclusions", extension.exclusions)
    }

    @TaskAction
    def invoke() {
        if (!extension.builder) {
            throw new ContainerException("OSGi container type not specified.")
        }

        logger.info "Creating container"
        extension.builder.main()

        logger.info "Preparing bundles"
        extension.builder.bundles()

        logger.info "Preparing configuration"
        extension.builder.configs()

        logger.info "Preparing runner scripts"
        extension.builder.scripts()

        logger.info "Container build properly"
    }
}
