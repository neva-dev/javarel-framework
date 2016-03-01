package com.neva.gradle.osgi.container.task

import org.gradle.api.tasks.TaskAction

class BuildTask extends ContainerTask {

    static final NAME = "buildOsgiContainer"

    BuildTask() {
        inputs.properties(extension.config)
        inputs.files(extension.runners)
        outputs.dir(extension.containerDir)
    }

    @TaskAction
    def invoke() {
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
