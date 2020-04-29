package com.git.reny.lib_base.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.git.reny.lib_base.base.utils.ImgUtils
import com.git.reny.lib_base.base.utils.PermissionHelper
import com.jaeger.library.StatusBarUtil

open class RBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppManager.instance.addActivity(this)
        initBase()
        setStatusBar(isTransStatusBar(), isDarkMode())
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.finishActivity(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ImgUtils.loader.onTrimMemory(this, level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ImgUtils.loader.onLowMemory(this)
    }

    open fun isTransStatusBar():Boolean = false
    open fun isDarkMode():Boolean = false

}

fun Activity.initBase(){
    ARouter.getInstance().inject(this);
}

fun Activity.setStatusBar(isTransStatusBar:Boolean, isDarkMode:Boolean = false){
    if (isTransStatusBar) {
        //状态栏透明
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
        if (isDarkMode) {
            //状态栏文字为白色
            StatusBarUtil.setDarkMode(this)
        }
    } else {
        //android 6.0以上  设置LightMode  状态栏文字为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setLightMode(this)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}