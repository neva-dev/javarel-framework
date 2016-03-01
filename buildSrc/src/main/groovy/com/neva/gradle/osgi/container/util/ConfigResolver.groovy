package com.neva.gradle.osgi.container.util

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency

final class ConfigResolver {

    static Collection<ProjectDependency> spreadDeps(Configuration config) {
        def projDeps = config.allDependencies.findAll {
            it instanceof ProjectDependency
        } as Collection<ProjectDependency>

        return flattenDeps(projDeps)
    }

    static Collection<ProjectDependency> flattenDeps(Collection<ProjectDependency> projDeps) {
        def result = new HashSet<ProjectDependency>(projDeps)
        for (ProjectDependency projDep : projDeps) {
            def deps = spreadDeps(projDep)
            result.addAll(deps)
        }

        return result
    }

    static Collection<ProjectDependency> spreadDeps(ProjectDependency projDep) {
        def projDeps = projDep.projectConfiguration.allDependencies.findAll {
            it instanceof ProjectDependency
        } as Collection<ProjectDependency>

        return flattenDeps(projDeps)
    }

}
