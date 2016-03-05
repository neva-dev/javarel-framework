package com.neva.gradle.osgi.container.util

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class BundleDetector {

    static boolean isBundle(File file) {
        checkManifest(file, {
            it.trim().startsWith('Bundle')
        })
    }

    static boolean isFragment(file) {
        checkManifest(file, {
            it.trim().startsWith('Fragment-Host')
        })
    }

    static boolean checkManifest(File file, Closure check) {
        def zip = new ZipFile(file)
        try {
            ZipEntry entry = zip.getEntry('META-INF/MANIFEST.MF')
            if (!entry) return true
            def lines = zip.getInputStream(entry).readLines()
            return lines.any(check)
        } finally {
            zip.close()
        }
    }

}
