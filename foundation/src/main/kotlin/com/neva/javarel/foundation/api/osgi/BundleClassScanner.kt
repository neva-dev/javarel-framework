package com.neva.javarel.foundation.api.osgi

import org.apache.commons.lang3.StringUtils
import org.osgi.framework.Bundle
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*

/**
 * Utility class for searching classes in specified bundle
 * If bundle context is provided it can also support fragment bundles
 */
class BundleClassScanner(val bundle: Bundle) {

    companion object {
        private val LOG = LoggerFactory.getLogger(BundleClassScanner::class.java)
    }

    fun findClasses(packageName: String): List<Class<*>> {
        @SuppressWarnings("unchecked")
        val classUrls = bundle.findEntries(packageName.replace('.', '/'), "*.class", true)
        val bundleName = bundle.symbolicName
        val classes = ArrayList<Class<*>>()

        if (classUrls == null) {
            LOG.warn("No classes found in bundle: {}", bundleName)
        } else {
            while (classUrls.hasMoreElements()) {
                val url = classUrls.nextElement()
                val className = toClassName(url)

                try {
                    if (BundleUtils.isFragment(bundle)) {
                        val hostBundle = BundleUtils.getHostBundle(bundle)

                        if (hostBundle == null) {
                            LOG.warn("Cannot find host bundle for {}", bundleName)
                        } else {
                            classes.add(hostBundle.loadClass(className))
                        }
                    } else {
                        classes.add(bundle.loadClass(className))
                    }
                } catch (e: ClassNotFoundException) {
                    LOG.warn("Unable to load class", e)
                } catch (e: NoClassDefFoundError) {
                    LOG.warn("Unable to load class", e)
                }
            }
        }

        return classes
    }

    fun findClasses(packages: List<String> = Arrays.asList("/"), annotation: Class<out Annotation>? = null): List<Class<*>> {
        val classes = ArrayList<Class<*>>()

        for (packageName in packages) {
            for (clazz in findClasses(packageName)) {
                if (annotation == null || clazz.isAnnotationPresent(annotation)) {
                    classes.add(clazz)
                }
            }
        }

        return classes
    }

    fun findClasses(header: String, annotation: Class<out Annotation>): List<Class<*>> {
        return findClasses(parsePackagesFromHeader(header), annotation)
    }

    fun findClasses(annotation: Class<out Annotation>): List<Class<*>> {
        return findClasses(Arrays.asList("/"), annotation)
    }

    private fun toClassName(url: URL): String {
        val f = url.file
        val cn = f.substring(1, f.length - ".class".length)
        return cn.replace('/', '.')
    }

    private fun parsePackagesFromHeader(header: String): List<String> {
        val values = bundle.headers.get(header) ?: ""
        val packages = StringUtils.deleteWhitespace(values).split(";")

        return Arrays.asList<String>(*packages.toTypedArray())
    }
}