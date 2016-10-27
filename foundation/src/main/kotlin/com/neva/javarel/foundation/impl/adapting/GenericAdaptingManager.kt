package com.neva.javarel.foundation.impl.adapting

import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.neva.javarel.foundation.api.adapting.Adapter
import com.neva.javarel.foundation.api.adapting.AdaptingException
import com.neva.javarel.foundation.api.adapting.AdaptingManager
import org.apache.felix.scr.annotations.*
import kotlin.reflect.KClass

@Component(immediate = true)
@Service
class GenericAdaptingManager : AdaptingManager {

    @Reference(
            referenceInterface = Adapter::class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    override val adapters = Maps.newConcurrentMap<KClass<Any>, MutableSet<Adapter<Any, Any>>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> adapt(adaptable: Any, clazz: KClass<T>): T {
        val sourceType = adaptable.javaClass.kotlin
        val targetType = clazz as KClass<Any>

        val adapter = findAdapter(sourceType, targetType)
        if (adapter == null) {
            throw AdaptingException("Adapting type: '${sourceType.qualifiedName}' into '${targetType.qualifiedName}' is not possible.")
        }

        return adapter.adapt(adaptable) as T
    }

    private fun findAdapter(sourceType: KClass<Any>?, targetType: KClass<Any>): Adapter<Any, Any>? {
        var fallback: Adapter<Any, Any>? = null

        if (adapters.containsKey(targetType)) {
            for (adapter in adapters.get(targetType)!!) {
                if (adapter.sourceTypes.contains(sourceType)) {
                    return adapter
                } else {
                    fallback = adapter
                }
            }
        }

        return fallback
    }

    private fun bindAdapters(adapter: Adapter<Any, Any>) {
        adapters.getOrPut(adapter.targetType, { Sets.newConcurrentHashSet() }).add(adapter)
    }

    private fun unbindAdapters(adapter: Adapter<Any, Any>) {
        adapters.get(adapter.targetType)!!.remove(adapter)
    }

}
