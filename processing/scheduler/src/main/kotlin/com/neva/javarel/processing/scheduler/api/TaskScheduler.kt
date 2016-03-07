package com.neva.javarel.processing.scheduler.api

import org.quartz.Scheduler

interface TaskScheduler {

    fun getScheduler() : Scheduler

}