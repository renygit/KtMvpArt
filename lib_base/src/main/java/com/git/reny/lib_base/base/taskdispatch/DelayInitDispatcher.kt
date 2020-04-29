package com.git.reny.lib_base.base.taskdispatch

import android.os.Looper
import android.os.MessageQueue.IdleHandler
import com.git.reny.lib_base.base.taskdispatch.task.DispatchRunnable
import com.git.reny.lib_base.base.taskdispatch.task.Task
import java.util.*

class DelayInitDispatcher {
    private val mDelayTasks: Queue<Task> = LinkedList<Task>()

    /**
     * IdleHandler：在系统空闲后执行
     */
    private val mIdleHandler = //系统空闲时回调
        IdleHandler {
            if (mDelayTasks.size > 0) {
                val task: Task = mDelayTasks.poll()!!
                DispatchRunnable(task).run()
            }
            !mDelayTasks.isEmpty()
        }

    fun addTask(task: Task): DelayInitDispatcher {
        mDelayTasks.add(task)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(mIdleHandler)
    }
}