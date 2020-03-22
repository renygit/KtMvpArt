package com.git.reny.lib_base.base.utils

import com.git.reny.lib_base.base.App
import org.jetbrains.anko.connectivityManager

class NetUtils {

    companion object{
        //TODO 暂时这么用  引入事件总线后 修改
        fun isConn():Boolean{
            App.instance.connectivityManager.activeNetworkInfo?.let {
                return it.isConnected
            }
            return false
        }
    }

}