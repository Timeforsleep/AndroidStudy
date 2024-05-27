package com.cxz.wanandroid.rx

import com.cxz.wanandroid.rx.scheduler.IoMainScheduler

/**
 * Created by guoyikai on 2023/4/21.
 */
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

}