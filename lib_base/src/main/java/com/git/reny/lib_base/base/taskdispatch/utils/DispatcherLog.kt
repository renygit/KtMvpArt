package com.git.reny.lib_base.base.taskdispatch.utils

import timber.log.Timber

object DispatcherLog {
    var isDebug = true
    fun i(msg: String?) {
        if (!isDebug) {
            return
        }
        msg?.let {
            Timber.i(it)
        }
    }

}