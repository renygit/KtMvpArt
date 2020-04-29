package com.git.reny.lib_base.base.app_task

import com.git.reny.lib_base.base.taskdispatch.task.Task
import timber.log.Timber

class InitTimber(private val isDebug:Boolean) : Task() {

    override fun needWait(): Boolean = true

    override fun run() {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(object : Timber.Tree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?
                ) {
                    Timber.d(tag, message)
                }
            })
        }

        Timber.tag("Timber")
    }

}