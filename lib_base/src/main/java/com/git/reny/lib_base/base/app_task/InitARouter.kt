package com.git.reny.lib_base.base.app_task

import com.alibaba.android.arouter.launcher.ARouter
import com.git.reny.lib_base.base.App
import com.git.reny.lib_base.base.taskdispatch.task.Task

class InitARouter(private val isDebug:Boolean) : Task() {

    override fun needWait(): Boolean = true

    override fun run() {
        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(App.instance);
    }

}