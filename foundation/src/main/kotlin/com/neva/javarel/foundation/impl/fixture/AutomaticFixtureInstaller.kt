package com.neva.javarel.foundation.impl.fixture

import com.google.common.collect.Sets
import com.neva.javarel.foundation.api.fixture.Fixture
import com.neva.javarel.foundation.api.fixture.FixtureManager
import com.neva.javarel.foundation.api.osgi.BundleUtils
import com.neva.javarel.foundation.api.scanning.BundleWatcher
import org.apache.felix.scr.annotations.*
import org.osgi.framework.BundleEvent
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDateTime

@Component(immediate = true)
@Service(FixtureManager::class, BundleWatcher::class)
class AutomaticFixtureInstaller : FixtureManager, BundleWatcher {

    companion object {
        val LOG = LoggerFactory.getLogger(AutomaticFixtureInstaller::class.java)
        val LOCK_DIR = "fixture"
    }

    @Reference(
            referenceInterface = Fixture::class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )

    private var fixtures = Sets.newTreeSet<Fixture>({ f1, f2 -> f1.order().compareTo(f2.order()) })

    private fun bindFixtures(fixture: Fixture) {
        fixtures.add(fixture)
    }

    private fun unbindFixtures(fixture: Fixture) {
        fixtures.remove(fixture)
    }

    override fun watch(event: BundleEvent) {
        if (allBundlesActive(event)) {
            installAll()
        }
    }

    private fun allBundlesActive(event: BundleEvent): Boolean {
        return event.bundle.bundleContext.bundles.all { BundleUtils.isActive(it) }
    }

    @Synchronized
    private fun installAll() {
        fixtures.forEach { fixture ->
            val clazz = fixture.javaClass.canonicalName
            val lock = File(lockDir, "$clazz.lock")

            if (!lock.exists()) {
                LOG.info("Installing fixture: {}", clazz)

                try {
                    fixture.install()

                    lock.createNewFile()
                    lock.writeText("Installed at ${LocalDateTime.now()}")
                } catch (e: Throwable) {
                    LOG.error("Fixture cannot be installed properly '$clazz'", e)
                }
            }
        }
    }

    @Synchronized
    private fun uninstallAll() {
        fixtures.reversed().forEach { fixture ->
            val clazz = fixture.javaClass.canonicalName
            val lock = File(lockDir, "$clazz.lock")

            if (lock.exists()) {
                LOG.info("Uninstalling fixture: {}", fixture.javaClass.canonicalName)

                try {
                    fixture.uninstall()
                    lock.delete()
                } catch (e: Throwable) {
                    LOG.error("Fixture cannot be uninstalled properly '$clazz'", e)
                }
            }
        }
    }

    private val lockDir: File by lazy {
        val dir = File(LOCK_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        dir
    }

    override fun reinstall() {
        uninstallAll()
        installAll()
    }

}