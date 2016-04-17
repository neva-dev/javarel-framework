package com.neva.gradle.osgi.container.task

import com.neva.gradle.osgi.container.ContainerConfig
import com.neva.gradle.osgi.container.ContainerException
import com.neva.gradle.osgi.container.util.DependencyResolver
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.TaskAction

class CreateContainerTask extends ContainerTask {

    static final NAME = "createOsgiContainer"

    CreateContainerTask() {
        inputs.files(extension.runners)
        outputs.dir(extension.containerDir)

        inputs.properties(extension.config)
        inputs.property("exclusions", extension.exclusions)

        configureModules()
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

    private configureModules() {
        def task = this
        project.afterEvaluate {
            def moduleConfig = project.configurations.getByName(ContainerConfig.MODULE)
            def moduleDeps = DependencyResolver.spread(moduleConfig)

            moduleDeps.each { ProjectDependency projectDependency ->
                def subProject = projectDependency.dependencyProject
                task.dependsOn(subProject.tasks.getByName(BasePlugin.ASSEMBLE_TASK_NAME))
            }
        }
    }
}
