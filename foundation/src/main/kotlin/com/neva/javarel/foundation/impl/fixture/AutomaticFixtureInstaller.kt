package com.neva.javarel.foundation.impl.fixture

import com.google.common.collect.Lists
import com.neva.javarel.foundation.api.fixture.Fixture
import com.neva.javarel.foundation.api.fixture.FixtureManager
import com.neva.javarel.foundation.api.fixture.LockFixture
import org.apache.felix.scr.annotations.*
import org.slf4j.LoggerFactory

@Component(immediate = true)
@Service(FixtureManager::class)
class AutomaticFixtureInstaller : FixtureManager {

    companion object {
        val LOG = LoggerFactory.getLogger(AutomaticFixtureInstaller::class.java)
        val LOCK_DIR = "fixture"
    }

    @Reference(
            referenceInterface = Fixture::class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private var fixtures = Lists.newCopyOnWriteArrayList<Fixture>()

    private fun bindFixtures(fixture: Fixture) {
        val locked = LockFixture(fixture)

        fixtures.add(locked)
        locked.install()
    }

    private fun unbindFixtures(fixture: Fixture) {
        fixtures.remove(fixture)
    }

    private fun installAll() {
        fixtures.forEach(Fixture::install)
    }

    private fun uninstallAll() {
        fixtures.reversed().forEach(Fixture::uninstall)
    }

    @Synchronized
    override fun reinstall() {
        uninstallAll()
        installAll()
    }

}