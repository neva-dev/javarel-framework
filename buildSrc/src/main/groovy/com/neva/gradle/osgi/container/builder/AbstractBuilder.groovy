package com.neva.gradle.osgi.container.builder

import com.neva.gradle.osgi.container.ContainerConfig
import com.neva.gradle.osgi.container.ContainerException
import com.neva.gradle.osgi.container.ContainerExtension

import com.neva.gradle.osgi.container.util.ConfigResolver
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

class AbstractBuilder implements ContainerBuilder {

    Project project

    AbstractBuilder(Project project) {
        this.project = project
    }

    @Override
    def main() {
        def config = project.configurations.getByName(ContainerConfig.MAIN)
        def size = config.dependencies.size()

        if (size == 0) {
            throw new ContainerException("Configuration named '${ContainerConfig.MAIN}' should have container main dependency defined.")
        } else if (size != 1) {
            throw new ContainerException("Configuration named '${ContainerConfig.MAIN}' cannot have more than one dependency defined.")
        }

        project.copy {
            from config
            into extension.containerDir
        }
    }

    @Override
    def bundles() {
        def moduleConfig = project.configurations.getByName(ContainerConfig.MODULE)
        def moduleDeps = ConfigResolver.spreadDeps(moduleConfig)
        def moduleJars = moduleConfig.resolve()

        def allBundles = new HashSet<>(moduleJars)
        def allJars = new HashSet<>()

        moduleDeps.each { ProjectDependency projectDependency ->
            def subProject = projectDependency.dependencyProject
            def subBundles = subProject.configurations.getByName(ContainerConfig.BUNDLE).resolve()
            def subJars = subProject.configurations.getByName(ContainerConfig.WRAP).resolve()

            allBundles += subBundles
            allJars += subJars
        }

        copyBundles(allBundles)
        wrapBundles(allJars)
    }

    def copyBundles(Collection<File> files) {
        project.copy {
            from files
            into bundleDir
        }
    }

    def wrapBundles(Collection<File> jars) {
        jars.each { File jar ->
            //BundleWrapper.wrapNonBundle(jar, bundleDir)
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

    String getBundleDir() {
        "${extension.containerDir}/${extension.bundlePath}"
    }

    ContainerExtension getExtension() {
        project.extensions.getByName(ContainerExtension.NAME) as ContainerExtension
    }

}
