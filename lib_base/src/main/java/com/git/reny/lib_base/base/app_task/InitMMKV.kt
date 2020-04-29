package com.git.reny.lib_base.base.app_task

import com.git.reny.lib_base.base.App
import com.git.reny.lib_base.base.extras.loge
import com.git.reny.lib_base.base.taskdispatch.task.Task
import com.tencent.mmkv.MMKV


class InitMMKV() : Task() {

    override fun needWait(): Boolean = true

    override fun dependsOn(): List<Class<out Task?>>? {
        return listOf(InitTimber::class.java)
    }

    override fun run() {
        val rootDir: String = MMKV.initialize(App.instance)
        loge("mmkv root: $rootDir")
    }

}