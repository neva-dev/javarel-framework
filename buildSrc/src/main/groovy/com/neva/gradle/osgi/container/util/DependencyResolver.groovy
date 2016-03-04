package com.neva.gradle.osgi.container.util

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency

final class DependencyResolver {

    static Collection<ProjectDependency> spread(Configuration config) {
        def projDeps = config.allDependencies.findAll {
            it instanceof ProjectDependency
        } as Collection<ProjectDependency>

        return flatten(projDeps)
    }

    static Collection<ProjectDependency> flatten(Collection<ProjectDependency> projDeps) {
        def result = new HashSet<ProjectDependency>(projDeps)
        for (ProjectDependency projDep : projDeps) {
            def deps = spread(projDep)
            result.addAll(deps)
        }

        return result
    }

    static Collection<ProjectDependency> spread(ProjectDependency projDep) {
        def projDeps = projDep.projectConfiguration.allDependencies.findAll {
            it instanceof ProjectDependency
        } as Collection<ProjectDependency>

        return flatten(projDeps)
    }

}
