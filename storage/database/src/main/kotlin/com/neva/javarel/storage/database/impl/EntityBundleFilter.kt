package com.neva.javarel.storage.impl

import com.neva.javarel.foundation.api.scanning.ComponentScanBundleFilter
import javax.persistence.Entity

class EntityBundleFilter : ComponentScanBundleFilter() {

    override val annotations: Set<Class<out Annotation>>
        get() = setOf(Entity::class.java)

}