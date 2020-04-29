package com.git.reny.lib_base.base.utils

import android.content.Context
import android.net.ConnectivityManager
import com.git.reny.lib_base.base.App

class NetUtils {

    companion object{
        //TODO 暂时这么用  引入事件总线后 修改
        fun isConn():Boolean{
            val manager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = manager.activeNetworkInfo

            // 判断网络情况
            if (networkInfo != null && networkInfo.isAvailable) {
                // 网络可用时的执行内容
                return true
            }
            /*App.instance.connectivityManager.activeNetworkInfo?.let {
                return it.isConnected
            }*/
            return false
        }
    }

}