package com.neva.gradle.openjpa

import com.neva.gradle.openjpa.task.EnhanceTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class OpenJpaPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task(EnhanceTask.NAME, type: EnhanceTask)
    }

}