package com.neva.javarel.security.auth.impl

import com.google.common.collect.Sets
import com.neva.javarel.security.auth.api.Auth
import org.apache.felix.scr.annotations.*
import org.apache.shiro.realm.Realm

@Component(immediate = true)
@Service(Auth::class)
class BasicAuth : Auth {

    @Reference(referenceInterface = Realm::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private var realms = Sets.newConcurrentHashSet<Realm>()

    private fun bindRealms(realm: Realm) {
        realms.add(realm)
    }

    private fun unbindRealms(realm: Realm) {
        realms.remove(realm)
    }
}