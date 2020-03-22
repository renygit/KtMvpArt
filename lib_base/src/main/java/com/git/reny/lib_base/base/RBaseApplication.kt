package com.git.reny.lib_base.base

import android.app.Application
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.git.reny.lib_base.R
import com.git.reny.lib_base.base.extras.getARouter
import com.git.reny.lib_base.base.extras.getMyDrawable
import com.git.reny.lib_imgload.ImgHelper
import com.git.reny.lib_imgload.core.ImgLoaderConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits
import timber.log.Timber
import timber.log.Timber.DebugTree


object App{
    lateinit var instance:Application
}

fun Application.initBase(isDebug: Boolean){
    App.instance = this

    initARouter(isDebug)
    initTimber(isDebug)
    initAutoSizeConfig()
    initRefreshStyle()
    initImgLoadConfig()
}

fun Application.onBaseTerminate(){
    getARouter().destroy()
}

private fun initARouter(isDebug: Boolean){
    if (isDebug) {
        ARouter.openLog();
        ARouter.openDebug();
    }
    ARouter.init(App.instance);
}

private fun initTimber(isDebug: Boolean){
    if (isDebug) {
        Timber.plant(DebugTree())
    } else {
        Timber.plant(object : Timber.Tree() {
            override fun log(
                priority: Int,
                tag: String?,
                message: String,
                t: Throwable?
            ) {
                Timber.d(tag, message)
            }
        })
    }

    Timber.tag("Timber")
}

private fun initAutoSizeConfig(){
    //不适配dp sp 启用mm单位 来适配屏幕 防止第三方控件用dp sp 造成影响
    AutoSizeConfig.getInstance().unitsManager
        .setSupportDP(false)
        .setSupportSP(false).supportSubunits = Subunits.MM
}

private fun initRefreshStyle() {
    //设置全局的Header构建器
    SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
        layout.setHeaderInsetStart(-8f)
        MaterialHeader(context).setColorSchemeColors(-0x1000000)
    }
    //设置全局的Footer构建器
    SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }
}

private fun initImgLoadConfig(){
    ImgHelper.initConfig(ImgLoaderConfig.Builder()
        .setPlace_holder(ContextCompat.getDrawable(App.instance, R.mipmap.img_holder)!!)
        .setCenterCrop(true)
        .builder())
}