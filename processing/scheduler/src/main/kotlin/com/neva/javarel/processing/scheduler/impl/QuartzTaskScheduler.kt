package com.neva.javarel.processing.scheduler.impl

import com.neva.javarel.processing.scheduler.api.Task
import com.neva.javarel.processing.scheduler.api.TaskScheduler
import org.apache.felix.ipojo.annotations.*
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory

@Component(immediate = true)
@Instantiate
@Provides
class QuartzTaskScheduler : TaskScheduler {

    private val scheduler = StdSchedulerFactory().getScheduler();

    override fun getScheduler(): Scheduler {
        return scheduler
    }

    @Validate
    fun validate() {
        scheduler.start()
    }

    @Invalidate
    fun invalidate() {
        scheduler.shutdown()
    }

    @Bind(aggregate = true)
    fun bindTask(task: Task) {
        scheduler.scheduleJob(task.job, task.trigger)
    }

    @Unbind
    fun unbindTask(task: Task) {
        scheduler.unscheduleJob(task.trigger.key)
    }

}