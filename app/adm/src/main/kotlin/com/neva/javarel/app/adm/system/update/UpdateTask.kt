package com.neva.javarel.app.adm.system.update

import com.neva.javarel.processing.scheduler.api.Task
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.quartz.*
import org.slf4j.LoggerFactory

@Component(immediate = true)
@Service
class UpdateTask : Task {

    companion object {
        val LOG = LoggerFactory.getLogger(UpdateTask::class.java)
    }

    override val trigger: Trigger
        get() {
            return TriggerBuilder
                    .newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/1 1/1 * ? *")) // every hour
                    .build();
        }

    override val job: JobDetail
        get() {
            return JobBuilder.newJob(UpdateJob::class.java)
                    .build()
        }

    class UpdateJob : Job {
        override fun execute(context: JobExecutionContext?) {
            LOG.info("Checking for system update...")
        }
    }
}