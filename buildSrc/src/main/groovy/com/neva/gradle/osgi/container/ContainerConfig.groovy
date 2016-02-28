package com.neva.gradle.osgi.container;

enum ContainerConfig {
    MAIN("osgiMain"),
    BUNDLE("osgiBundle"),
    WRAP("osgiWrap"),
    EMBED("osgiEmbed")

    String name

    ContainerConfig(String name) {
        this.name = name
    }
}