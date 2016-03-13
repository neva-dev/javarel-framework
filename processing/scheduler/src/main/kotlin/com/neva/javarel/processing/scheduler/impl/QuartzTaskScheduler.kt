package com.neva.javarel.processing.scheduler.impl

import com.neva.javarel.processing.scheduler.api.Task
import com.neva.javarel.processing.scheduler.api.TaskScheduler
import org.apache.felix.scr.annotations.*
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory

@Component(immediate = true)
@Service
class QuartzTaskScheduler : TaskScheduler {

    @Reference(referenceInterface = Task::class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
    private lateinit var tasks: Set<Task>

    private val scheduler = StdSchedulerFactory().getScheduler();

    override fun getScheduler(): Scheduler {
        return scheduler
    }

    @Activate
    protected fun start() {
        scheduler.start()
    }

    @Deactivate
    protected fun stop() {
        scheduler.shutdown()
    }

    protected fun bindTask(task: Task) {
        scheduler.scheduleJob(task.job, task.trigger)
    }

    protected fun unbindTask(task: Task) {
        scheduler.unscheduleJob(task.trigger.key)
    }

}