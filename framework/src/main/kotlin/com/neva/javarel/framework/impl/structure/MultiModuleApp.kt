package com.neva.javarel.framework.impl.structure

import com.neva.javarel.framework.api.structure.App
import com.neva.javarel.framework.api.structure.Module
import org.apache.felix.scr.annotations.*
import java.util.*

@Component(immediate = true)
@Service(App::class)
class MultiModuleApp : App {

    @Reference(
            referenceInterface = Module::class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    private val allModules = Collections.synchronizedSortedSet(TreeSet<Module>({ m1, m2 -> m1.priority.compareTo(m2.priority) }))

    override val modules: List<Module>
        get() = allModules.toList()

    protected fun bindModule(module: Module) {
        allModules.add(module)
    }

    protected fun unbindModule(module: Module) {
        allModules.remove(module)
    }

}