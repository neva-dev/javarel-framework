package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.processing.scheduler.api.Task
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.apache.felix.ipojo.annotations.Requires
import org.quartz.*
import org.slf4j.LoggerFactory

@Component(immediate = true)
@Instantiate
@Provides
class RestAppUpdateTask : Task {

    companion object {
        val logger = LoggerFactory.getLogger(RestAppUpdateTask::class.java)
    }

    @Requires
    lateinit var restApp: RestApplication

    private var componentCount = -1

    private val trigger = TriggerBuilder
            .newTrigger()
            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever())
            .build();


    private val job = JobBuilder.newJob(UpdateJob::class.java)
            .build()

    override fun getTrigger(): Trigger {
        return trigger
    }

    override fun getJob(): JobDetail {
        job.jobDataMap.put(RestAppUpdateTask::class.simpleName, this);

        return job
    }

    class UpdateJob : Job {

        override fun execute(context: JobExecutionContext?) {
            val task = context!!.jobDetail.jobDataMap.get(RestAppUpdateTask::class.simpleName) as RestAppUpdateTask;

            if (task.componentCount == -1 || task.restApp.getComponents().size != task.componentCount) {
                task.componentCount = task.restApp.getComponents().size

                logger.info("Updating rest application")
                task.restApp.updateHttpService()
            }
        }
    }
}