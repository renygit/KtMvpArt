package com.git.reny.ktapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.git.reny.lib_base.base.initBase
import com.git.reny.lib_base.base.onBaseTerminate

class MyApp : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()

        initBase(true)
    }

    override fun onTerminate() {
        super.onTerminate()
        onBaseTerminate()
    }

}