package com.neva.javarel.processing.scheduler.impl

import com.neva.javarel.foundation.api.lang.ReflectionUtils
import com.neva.javarel.foundation.api.injection.Osgi
import com.neva.javarel.foundation.api.osgi.OsgiUtils
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.SchedulerException
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle
import org.slf4j.LoggerFactory

class OsgiJobFactory : JobFactory {

    companion object {
        val LOG = LoggerFactory.getLogger(OsgiJobFactory::class.java)
    }

    override fun newJob(bundle: TriggerFiredBundle, scheduler: Scheduler): Job {
        val jobDetail = bundle.jobDetail
        val clazz = jobDetail.jobClass

        if (LOG.isDebugEnabled) {
            LOG.debug("Producing instance of Job '" + jobDetail.key + "', class=" + clazz.name)
        }

        val job = try {
            clazz.newInstance()
        } catch (e: Exception) {
            throw SchedulerException("Problem instantiating class '" + clazz.name + "'", e)
        }

        ReflectionUtils.getInheritedFields(job.javaClass).forEach { field ->
            if (field.isAnnotationPresent(Osgi::class.java)) {
                val service = OsgiUtils().serviceOf(field.type)
                val accessible = field.isAccessible

                field.isAccessible = true
                field.set(job, service)
                field.isAccessible = accessible
            }
        }

        return job
    }

}