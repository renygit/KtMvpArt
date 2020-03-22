package com.git.reny.lib_base.base.extras

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.git.reny.lib_base.base.App
import com.git.reny.lib_base.base.utils.ClickUtils
import es.dmoral.toasty.Toasty
import timber.log.Timber

fun Any.getARouter() : ARouter{
    return ARouter.getInstance()
}

fun toast(text:Any, context: Context, duration:Int){
    if(text is String) {
        Toasty.normal(context, text, duration).show()
        //Toast.makeText(context, text, duration).show()
    }
    if(text is Int){
        Toasty.normal(context, text, duration).show()
        //Toast.makeText(context, text, duration).show()
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

fun Any.singleClick(clickFunc: () -> Unit){
    if(!ClickUtils.isFastClick()){
        clickFunc()
    }
}







