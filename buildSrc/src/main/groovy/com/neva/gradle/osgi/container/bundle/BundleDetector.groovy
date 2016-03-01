package com.neva.gradle.osgi.container.bundle

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class BundleDetector {

    static boolean notBundle(File file) {
        def zip = new ZipFile(file)
        try {
            ZipEntry entry = zip.getEntry('META-INF/MANIFEST.MF')
            if (!entry) return true
            def lines = zip.getInputStream(entry).readLines()
            return !lines.any { it.trim().startsWith('Bundle') }
        } finally {
            zip.close()
        }
    }

    static boolean isBundle(File file) {
        !notBundle(file)
    }

    static boolean isFragment(file) {
        def zip = new ZipFile(file as File)
        try {
            ZipEntry entry = zip.getEntry('META-INF/MANIFEST.MF')
            if (!entry) return true
            def lines = zip.getInputStream(entry).readLines()
            return lines.any { it.trim().startsWith('Fragment-Host') }
        } finally {
            zip.close()
        }
    }

}
