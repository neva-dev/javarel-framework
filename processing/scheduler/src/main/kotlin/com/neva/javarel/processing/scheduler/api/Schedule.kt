package com.neva.javarel.processing.scheduler.api

import org.quartz.JobDetail
import org.quartz.Trigger

interface Schedule {

    val trigger: Trigger

    val job: JobDetail

}