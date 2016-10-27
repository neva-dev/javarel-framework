package com.neva.javarel.foundation.api.fixture

import com.neva.javarel.foundation.impl.fixture.AutomaticFixtureInstaller
import java.io.File
import java.time.LocalDateTime

/**
 * Fixture that can be only installed or uninstalled depending on existence of lock file in filesystem.
 */
class LockFixture(val base: Fixture) : Fixture {

    override fun install() {
        val lock = File(lockDir, "$name.lock")

        if (!lock.exists()) {
            AutomaticFixtureInstaller.LOG.info("Installing fixture: {}", name)

            try {
                base.install()

                lock.createNewFile()
                lock.writeText("Installed at ${LocalDateTime.now()}")
            } catch (e: Throwable) {
                AutomaticFixtureInstaller.LOG.error("Fixture cannot be installed properly '$name'", e)
            }
        }
    }

    override fun uninstall() {
        val lock = File(lockDir, "$name.lock")

        if (lock.exists()) {
            AutomaticFixtureInstaller.LOG.info("Uninstalling fixture: {}", name)

            try {
                base.uninstall()
                lock.delete()
            } catch (e: Throwable) {
                AutomaticFixtureInstaller.LOG.error("Fixture cannot be uninstalled properly '$name'", e)
            }
        }
    }

    private val name: String
        get() = base.javaClass.canonicalName

    private val lockDir: File by lazy {
        val dir = File(AutomaticFixtureInstaller.LOCK_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        dir
    }

}