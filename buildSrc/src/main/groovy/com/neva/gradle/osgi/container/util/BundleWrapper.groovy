package com.neva.gradle.osgi.container.util


import aQute.bnd.osgi.Analyzer
import aQute.bnd.osgi.Jar
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import java.util.jar.Manifest
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class BundleWrapper {

    static final Logger log = Logging.getLogger(BundleWrapper)

    static void wrapNonBundle(File jarFile, String bundlesDir) {
        log.info "Wrapping non-bundle: {}", jarFile.name

        def newJar = new Jar(jarFile)
        def currentManifest = newJar.manifest

        Map<Object, Object[]> config = [:] // getWrapConfig(wrapInstructions, jarFile)
        def consumeValue = { String key ->
            Object[] items = config.remove(key)
            if (items) items.join(',')
            else null
        }

        if (!config) {
            log.info "No instructions provided to wrap bundle {}, will use defaults", jarFile.name
        }

        String implVersion = consumeValue('Bundle-Version') ?:
                currentManifest.mainAttributes.getValue('Implementation-Version') ?:
                        versionFromFileName(jarFile.name)

        String implTitle = consumeValue('Bundle-SymbolicName') ?:
                currentManifest.mainAttributes.getValue('Implementation-Title') ?:
                        titleFromFileName(jarFile.name)

        String imports = consumeValue('Import-Package') ?: '*'
        String exports = consumeValue('Export-Package') ?: '*'

        def analyzer = new Analyzer().with {
            jar = newJar
            bundleVersion = implVersion
            bundleSymbolicName = implTitle
            importPackage = imports
            exportPackage = exports
            config.each { k, v -> it.setProperty(k as String, v.join(',')) }
            return it
        }

        Manifest manifest = analyzer.calcManifest()

/*        if (wrapInstructions.printManifests) {
            println " Manifest for ${jarFile.name} ".center(100, '-')
            manifest.write(System.out)
            println '-' * 100
        }*/

        def bundle = new File("$bundlesDir/${jarFile.name}")

        copyJar(jarFile, bundle) {
            ZipFile input, ZipOutputStream out, ZipEntry entry ->
                if (entry.name == 'META-INF/MANIFEST.MF') {
                    out.putNextEntry(new ZipEntry(entry.name))
                    manifest.write(out)
                } else {
                    out.putNextEntry(entry)
                    out.write(input.getInputStream(entry).bytes)
                }
        }
    }

    static void copyJar(File source, File destination,
                        Closure copyFunction,
                        Closure afterFunction = { _ -> }) {
        def destinationStream = new ZipOutputStream(destination.newOutputStream())
        def input = new ZipFile(source)
        try {
            for (entry in input.entries()) {
                copyFunction(input, destinationStream, entry)
            }
            afterFunction(destinationStream)
        } finally {
            try {
                destinationStream.close()
            } catch (ignored) {
            }
            try {
                input.close()
            } catch (ignored) {
            }
        }
    }
/*
    private static Map<String, Object[]> getWrapConfig(
            WrapInstructionsConfig wrapInstructions, File jarFile) {
        Map<String, Object[]> config = wrapInstructions.manifests.find { regx, _ ->
            try {
                def match = jarFile.name ==~ regx
                if (match) log.debug('Regex {} matched jar file {}', regx, jarFile.name)
                return match
            } catch (e) {
                log.warn('Invalid regex in wrapInstructions Map: {}', e as String)
                return false
            }
        }?.value

        config ?: [:]
    }*/

    static String removeExtensionFrom(String name) {
        def dot = name.lastIndexOf('.')
        if (dot > 0) { // exclude extension
            return name[0..<dot]
        }
        return name
    }

    static String versionFromFileName(String name) {
        name = removeExtensionFrom(name)
        def digitsAfterDash = name.find(/\-\d+.*/)
        if (digitsAfterDash) {
            return digitsAfterDash[1..-1] // without the dash
        }
        int digit = name.findIndexOf { it.number }
        if (digit > 0) {
            return name[digit..-1]
        }
        '1.0.0'
    }

    static String titleFromFileName(String name) {
        name = removeExtensionFrom(name)
        def digitsAfterDash = name.find(/\-\d+.*/)
        if (digitsAfterDash) {
            return name - digitsAfterDash
        }
        int digit = name.findIndexOf { it.number }
        if (digit > 0) {
            return name[0..<digit]
        }
        name
    }


}