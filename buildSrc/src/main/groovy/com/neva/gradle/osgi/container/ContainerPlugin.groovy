package com.neva.gradle.osgi.container

import com.neva.gradle.osgi.container.task.BuildTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class ContainerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply(ModulePlugin)
        project.extensions.create(ContainerExtension.NAME, ContainerExtension, project)

        project.configurations.create(ContainerConfig.MODULE, {
            it.transitive = false
        })
        project.configurations.create(ContainerConfig.MAIN, {
            it.transitive = false
        })

        project.dependencies.add(ContainerConfig.MODULE, project)

        project.task(BuildTask.NAME, type: BuildTask)
    }
}
