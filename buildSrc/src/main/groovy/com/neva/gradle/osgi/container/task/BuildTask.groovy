package com.neva.gradle.osgi.container.task

import org.gradle.api.tasks.TaskAction

class BuildTask extends ContainerTask {

    static final NAME = "buildOsgiContainer"

    BuildTask() {
//        project.afterEvaluate {
//            inputs.properties(extension.config)
//            inputs.files(extension.runners)
//            outputs.dir(extension.containerDir)
//        }
    }

    @TaskAction
    def invoke() {
        logger.info "Container build properly in '${extension.containerDir}'"
    }

}
