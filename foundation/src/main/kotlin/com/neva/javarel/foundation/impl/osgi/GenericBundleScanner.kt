package com.neva.javarel.foundation.impl.osgi

import com.google.common.collect.Lists
import com.neva.javarel.foundation.api.osgi.BundleFilter
import com.neva.javarel.foundation.api.osgi.BundleScanner
import com.neva.javarel.foundation.api.osgi.BundleUtils
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.objectweb.asm.ClassReader
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL

@Service(BundleScanner::class)
@Component(immediate = true)
class GenericBundleScanner : BundleScanner {

    companion object {
        val log = LoggerFactory.getLogger(GenericBundleScanner::class.java)
    }

    private var context: BundleContext? = null

    @Activate
    protected fun activate(context: BundleContext) {
        this.context = context
    }

    override fun scan(filter: BundleFilter): Collection<Class<*>> {
        val classes = mutableListOf<Class<*>>()

        for (bundle in context!!.bundles) {
            if (!filter.filterBundle(bundle)) {
                continue
            }

            val urls = collectUrls(bundle)
            for (url in urls) {
                try {
                    val classReader = ClassReader(url.openStream())
                    if (filter.filterClass(bundle, classReader)) {
                        val className = BundleUtils.toClassName(url)
                        classes.add(bundle.loadClass(className))
                    }
                } catch (e: IOException) {
                    log.warn("Class cannot be read: '$url'.")
                } catch (e: ClassNotFoundException) {
                    log.warn("Class not found: '$url'")
                }
            }
        }

        return classes.toList()
    }

    private fun collectUrls(bundle: Bundle): List<URL> {
        val classNames = Lists.newArrayList<URL>()

        val classUrls = bundle.findEntries("/", "*.class", true)
        if (classUrls != null) {
            while (classUrls.hasMoreElements()) {
                classNames.add(classUrls.nextElement())
            }
        }

        return classNames
    }
}