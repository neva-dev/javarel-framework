package com.neva.javarel.communication.rest.impl

import com.neva.javarel.communication.rest.api.RestApplication
import com.neva.javarel.foundation.osgi.ServiceUtils
import com.neva.javarel.processing.scheduler.api.Task
import org.apache.felix.ipojo.annotations.Component
import org.apache.felix.ipojo.annotations.Instantiate
import org.apache.felix.ipojo.annotations.Provides
import org.quartz.*
import org.slf4j.LoggerFactory

@Component(immediate = true)
@Instantiate
@Provides
class RestAppUpdateTask : Task {

    companion object {
        val logger = LoggerFactory.getLogger(RestAppUpdateTask::class.java)
    }

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
        return job
    }

    class UpdateJob : Job {
        override fun execute(context: JobExecutionContext?) {
            // TODO fix getService() == null, or pass injected restApplication somehow, update only component count changed
            ServiceUtils.getService(RestApplication::class).updateHttpService()
        }
    }
}