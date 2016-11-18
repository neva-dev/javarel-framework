package com.neva.javarel.processing.scheduler.api

import org.quartz.*
import java.util.*
import kotlin.reflect.KClass

abstract class BaseSchedule<T : Job> : Schedule {

    override val job: JobDetail
        get() = JobBuilder.newJob(jobType.java).build()

    abstract val jobType: KClass<T>

    override val trigger: Trigger
        get() {
            val result = TriggerBuilder.newTrigger()
            if (startAt != null) {
                result.startAt(startAt)
            }
            if (endAt != null) {
                result.endAt(endAt)
            }

            return result.withSchedule(schedule()).build()
        }

    protected open val startAt: Date? = null

    protected open val endAt: Date? = null

    abstract fun schedule(): ScheduleBuilder<*>

    protected fun cron(expression: String): ScheduleBuilder<*> = CronScheduleBuilder.cronSchedule(expression)

    protected fun repeat(how: (SimpleScheduleBuilder) -> ScheduleBuilder<*>): ScheduleBuilder<*> {
        return how(SimpleScheduleBuilder.simpleSchedule().repeatForever())
    }

    protected fun daily(how: (DailyTimeIntervalScheduleBuilder) -> ScheduleBuilder<*>): ScheduleBuilder<*> {
        return how(DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule())
    }

    protected fun calendar(how: (CalendarIntervalScheduleBuilder) -> ScheduleBuilder<*>): ScheduleBuilder<*> {
        return how(CalendarIntervalScheduleBuilder.calendarIntervalSchedule())
    }

}