package com.git.reny.mvp_ui.extras

import com.git.reny.lib_base.base.STATE_ERROR
import com.git.reny.lib_base.base.STATE_NO_NETWORK
import com.git.reny.lib_base.base.extras.isEmpty
import com.git.reny.lib_base.base.extras.loge
import com.git.reny.lib_base.base.extras.showToast
import com.git.reny.lib_base.base.utils.NetUtils
import com.git.reny.mvp_ui.MvpPresenter
import com.git.reny.mvp_ui.MvpView

//网络异常 UI统一处理
fun <V : MvpView> MvpPresenter<V>.exceptionHandle(popToast:Boolean = true, stateTip:String? = null) : ((Throwable) -> Unit)?{
    return {
        loge("${this.javaClass.simpleName} >> 异常 ${it.message}")
        if(popToast) {
            showToast(it.message, this.mView.get()?.getActivity())
        }
        if(isFirst){
            mView.get()?.getActivity()?.let {activity ->
                activity.runOnUiThread {
                    setLoadState(if(NetUtils.isConn()) STATE_ERROR else STATE_NO_NETWORK, hasMore = false, isRefresh = true)
                    if(!isEmpty(stateTip) && NetUtils.isConn()){//要想自定义网络未连接的提示 再修改逻辑
                        mView.get()?.getStateView()?.setTip(stateTip!!)
                    }
                }

            }
        }
    }
}