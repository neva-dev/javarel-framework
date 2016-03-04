package com.neva.gradle.osgi.container

import com.neva.gradle.osgi.container.builder.ContainerBuilder
import com.neva.gradle.osgi.container.builder.FelixBuilder
import org.gradle.api.Project

class ContainerExtension {

    static final NAME = 'osgiContainer'

    Project project

    ContainerBuilder builder

    Map<String, Object> config = [:]

    List<File> runners = []

    String bundlePath = 'bundle'

    String configFile = 'conf/config.properties'

    String containerDir = "build/osgiContainer"

    String javaCommand = 'java'

    String javaArgs = ''

    String programArgs = ''

    ContainerExtension(Project project) {
        this.project = project
    }

    def config(File file) {
        def props = new Properties()
        props.load(new FileInputStream(file))
        config.putAll(props as Map)
    }

    def config(String key, Object value) {
        config.put(key, value)
    }

    def felix() {
        builder = new FelixBuilder(project)
        bundlePath = 'bundle'
        runners += project.file("osgiContainer/felix/run.sh")
        config(project.file("osgiContainer/felix/config.properties"))
    }

    def debug(Integer port = 18080, Boolean suspend = true) {
        javaArgs = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=${suspend ? 'y' : 'n'},address=$port"
    }

    def String getJavaArgs() {
        return javaArgs.join(' ')
    }

    def String getProgramArgs() {
        return programArgs.join(' ')
    }

}
