package com.git.reny.mvp_ui

import android.content.Context
import android.view.View
import com.git.reny.common.widget.LoadingView


/***
 * 默认状态View
 * 想修改部分状态View 继承当前类  重写想修改的View
 */
class DefaultInitStateView(private val context:Context) : MultiStateView.InitStateView {

    override fun getLoadingId(): Int = 0
    override fun getErrorId(): Int = 0
    override fun getNoNetworkId(): Int = 0
    override fun getEmptyId(): Int = 0


    private var mStateView:StateView? = null
    private var mLoadingView: LoadingView? = null

    override fun getLoadingView(): View {
        if(mLoadingView == null){
            mLoadingView = LoadingView(context)
        }
        return mLoadingView!!
    }

    override fun getErrorView(): View {
        getStateView().setImg(R.mipmap.ic_error)
        getStateView().setTip("加载出错，请重试")
        return getStateView()
    }

    override fun getNoNetworkView(): View {
        getStateView().setImg(R.mipmap.ic_error)
        getStateView().setTip("网络链接失败，请重试")
        return getStateView()
    }

    override fun getEmptyView(): View {
        getStateView().setImg(R.mipmap.ic_empty)
        getStateView().setTip("暂无相关数据")
        return getStateView()
    }


    override fun getStateView():StateView{
        if(mStateView == null){
            mStateView = StateView(context)
        }
        return mStateView!!
    }
}