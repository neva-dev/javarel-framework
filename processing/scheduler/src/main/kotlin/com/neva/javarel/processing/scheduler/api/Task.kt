package com.neva.javarel.processing.scheduler.api

import org.quartz.JobDetail
import org.quartz.Trigger

interface Task {

    fun getTrigger(): Trigger

    fun getJob(): JobDetail

}