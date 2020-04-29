package com.git.reny.lib_base.base.extras

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.git.reny.lib_base.R
import com.git.reny.lib_base.base.App
import com.git.reny.lib_base.base.utils.ClickUtils
import com.tencent.mmkv.MMKV
import es.dmoral.toasty.Toasty
import timber.log.Timber

fun Any.getARouter() : ARouter{
    return ARouter.getInstance()
}


object ToastExt{
    var toast:Toast? = null
}
fun toast(text:Any, context: Context, duration:Int){
    ToastExt.toast?.cancel()
    if(text is String) {
        ToastExt.toast = Toasty.normal(context, text, duration)
        ToastExt.toast!!.show()
    }
    if(text is Int){
        ToastExt.toast = Toasty.normal(context, text, duration)
        ToastExt.toast!!.show()
    }
}

fun Any.showToast(text:Any?, activity: Activity? = null, duration:Int = Toast.LENGTH_LONG){
    if(text == null){
        loge("${this.javaClass.simpleName}>> showToast(null)")
        return
    }
    if(text is String || text is Int) {
        var act: Activity? = activity
        if(act == null){
            if(this is Activity) {
                act = this
            }else if(this is Fragment){
                act = this.activity
            }
        }

        if (act != null) {
            act.runOnUiThread {
                toast(text, act, duration)
            }
        } else {
            try {
                toast(text, App.instance, duration)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }else{
        throw RuntimeException("showToast text参数类型传入错误")
    }
}

fun Any.isEmpty(value: Any?):Boolean{
    if(value == null){
        return true
    }else{
        if(value is String){
            return value.isEmpty()
        }
        if(value is Collection<*>){
            return value.isEmpty()
        }
        if(value is Map<*, *>){
            return value.isEmpty()
        }
        return false
    }
}

fun Any.logd(str:String, other:Array<Any>? = null){
    Timber.d(str, other)
}
fun Any.loge(str:String, other:Array<Any>? = null){
    Timber.e(str, other)
}


fun View.onClick(clickFunc: () -> Unit){
    setOnClickListener {
        singleClick {
            clickFunc()
        }
    }
}

fun Any.singleClick(clickFunc: () -> Unit){
    if(!ClickUtils.isFastClick()){
        clickFunc()
    }
}


fun Any.setStatusHeight(status: View){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        try {
            //父类不是ViewGroup类型的会报错
            val lp: ViewGroup.LayoutParams = status.layoutParams
            lp.height = getDimenPx(R.dimen.status_bar_height, status.context)
            val resourceId: Int = status.context.resources
                .getIdentifier("status_bar_height", "dimen", "androidx")
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                lp.height = getDimenPx(resourceId, status.context)
            }
            status.layoutParams = lp
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

fun Any.getDimenPx(@DimenRes dimenId: Int, context: Context): Int {
    return context.resources.getDimensionPixelSize(dimenId)
}



/* 代替SharedPreferences
kv().encode("string", "Hello from mmkv");
String str = kv().decodeString("string");*/
private class MYKV private constructor(){
    companion object{
        val kv: MMKV = MMKV.defaultMMKV()
    }
}
fun Any.kv() : MMKV{
    return MYKV.kv
}







