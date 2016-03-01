package com.neva.gradle.osgi.container.builder

interface ContainerBuilder {

    def main()

    def bundles()

    def configs()

    def scripts()

}