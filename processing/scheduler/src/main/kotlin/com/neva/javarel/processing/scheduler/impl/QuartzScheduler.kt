package com.neva.javarel.processing.scheduler.impl

import com.neva.javarel.processing.scheduler.api.Schedule
import org.apache.felix.scr.annotations.*
import org.quartz.impl.StdSchedulerFactory
import org.quartz.Scheduler as BaseScheduler

@Component(immediate = true)
@Service
class QuartzScheduler : com.neva.javarel.processing.scheduler.api.Scheduler {

    @Reference(
            referenceInterface = Schedule::class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private lateinit var schedules: Set<Schedule>

    override val scheduler: BaseScheduler by lazy {
        val result = StdSchedulerFactory().scheduler
        result.setJobFactory(OsgiJobFactory())

        result
    }

    @Activate
    protected fun start() {
        scheduler.start()
    }

    @Deactivate
    protected fun stop() {
        scheduler.shutdown()
    }

    protected fun bindSchedule(schedule: Schedule) {
        scheduler.scheduleJob(schedule.job, schedule.trigger)
    }

    protected fun unbindSchedule(schedule: Schedule) {
        scheduler.unscheduleJob(schedule.trigger.key)
    }

}