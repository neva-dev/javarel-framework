package com.neva.gradle.openjpa

import org.apache.openjpa.enhance.PCEnhancer
import org.apache.openjpa.lib.util.Options
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class OpenJpaPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.extensions.create("openjpa", OpenJpaExtension)
        def task = target.tasks.create("openjpaEnhance")

        task.outputs.upToDateWhen { false }

        task.doLast {
            URL[] urls = collectURLs(target)
            def oldClassLoader = Thread.currentThread().getContextClassLoader()
            def loader = new URLClassLoader(urls, oldClassLoader)
            try {
                Thread.currentThread().setContextClassLoader(loader)
                FileCollection tree = target.openjpa.files;
                if (tree == null) {
                    tree = target.fileTree(target.sourceSets.main.output.classesDir)
                }
                logger.info("enhancing {}", tree.files);
                PCEnhancer.run(
                        tree.files as String[],
                        new Options(target.openjpa.toProperties())
                )
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader)
            }
        }

        task.dependsOn target.compileJava
        target.tasks.add task
        target.classes.dependsOn task
    }

    private static URL[] collectURLs(target) {
        Set<URL> compileClassPathURLs = []
        target.configurations.all({ config ->
            compileClassPathURLs.addAll(config.files.collect {
                it.toURI().toURL();
            })
        })

        def resourceUrls = target.sourceSets.main.resources.srcDirs.collect {
            return target.file(it).toURI().toURL()
        }
        def classesDir = target.sourceSets.main.output.classesDir
        return compileClassPathURLs + resourceUrls + classesDir.toURI().toURL()
    }

}