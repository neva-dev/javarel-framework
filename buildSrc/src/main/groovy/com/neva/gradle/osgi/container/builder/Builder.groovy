package com.neva.gradle.osgi.container.builder

interface Builder {

    def main()

    def bundles()

    def configs()

    def scripts()

}