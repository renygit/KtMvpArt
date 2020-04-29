package com.git.reny.lib_base.base.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

/**
 * Created by reny on 2019/10/9.
 */
object PermissionHelper {
    private var mapGrantedRunnable: MutableMap<Int, Runnable>? = null
    private var mapDeniedRunnable: MutableMap<Int, Runnable>? = null
    private var mRequestCode = 1

    fun requestPermissions(
        activity: Activity,
        grantedRunnable: Runnable,
        deniedRunnable: Runnable,
        vararg permissions: String
    ) {
        if (null == mapGrantedRunnable) {
            mapGrantedRunnable = HashMap(10)
        }
        if (null == mapDeniedRunnable) {
            mapDeniedRunnable = HashMap(10)
        }

        //用于存放为授权的权限
        val permissionList: MutableList<String> = ArrayList()
        //遍历传递过来的权限集合
        for (permission in permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission)
            }
        }

        //判断集合
        if (permissionList.isNotEmpty()) {  //如果集合不为空，则需要去授权
            mapGrantedRunnable!![mRequestCode] = grantedRunnable
            mapDeniedRunnable!![mRequestCode] = deniedRunnable
            ActivityCompat.requestPermissions(
                activity,
                permissionList.toTypedArray(),
                mRequestCode
            )
            mRequestCode++
        } else {  //为空，则已经全部授权
            Handler(Looper.getMainLooper()).post(grantedRunnable)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (mapGrantedRunnable!!.containsKey(requestCode)) {
            // 这里规定全部权限都通过才算通过
            var grant = true
            // 在A申请权限，然后马上跳转到B，则grantResults.length=0
            if (grantResults.isEmpty()) grant = false
            else {
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        grant = false
                    }
                }
            }
            if (grant) {
                Handler(Looper.getMainLooper())
                    .post(mapGrantedRunnable!![requestCode]!!)
            } else {
                Handler(Looper.getMainLooper())
                    .post(mapDeniedRunnable!![requestCode]!!)
            }
            mapGrantedRunnable!!.remove(requestCode)
            mapDeniedRunnable!!.remove(requestCode)
        }
    }
}