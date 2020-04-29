package com.git.reny.lib_base.base.taskdispatch.task

abstract class MainTask : Task() {
    override fun runOnMainThread(): Boolean {
        return true
    }
}