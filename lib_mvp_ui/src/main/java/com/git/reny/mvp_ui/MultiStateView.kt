package com.git.reny.mvp_ui

import android.content.Context
import android.view.View
import com.ethanhua.skeleton.ViewReplacer
import com.git.reny.lib_base.base.*


/***
 * 多状态View 基于骨架屏的库 ViewReplacer 实现  替换布局原理
 * 本身并不是View 只是操作传入进来的View 实现切换布局
 */
class MultiStateView(msv: View) {

    private var context: Context
    private var mViewReplacer: ViewReplacer
    private var mInitStateView: InitStateView

    init {
        if(msv.parent == null){
            throw Exception("setMultiStateView msv必须有父布局！详情查看ViewReplacer的原理")
        }
        context = msv.context
        mViewReplacer = ViewReplacer(msv)
        mInitStateView = DefaultInitStateView(context)
    }

    fun setInitStateView(initStateView: InitStateView){
        mInitStateView = initStateView
    }

    fun showLoading(){
        showState(STATE_LOADING)
    }

    fun showError(){
        showState(STATE_ERROR)
    }

    fun showNoNetwork(){
        showState(STATE_NO_NETWORK)
    }

    fun showEmpty(){
        showState(STATE_EMPTY)
    }

    fun showContent(){
        showState(STATE_CONTENT)
    }

    fun showState(@LoadState state: Int){
        when(state){
            STATE_LOADING -> {
                if(mInitStateView.getLoadingId() == 0){
                    mViewReplacer.replace(mInitStateView.getLoadingView())
                }else {
                    mViewReplacer.replace(mInitStateView.getLoadingId())
                }
            }
            STATE_ERROR -> {
                if(mInitStateView.getErrorId() == 0){
                    mViewReplacer.replace(mInitStateView.getErrorView())
                }else {
                    mViewReplacer.replace(mInitStateView.getErrorId())
                }
            }
            STATE_NO_NETWORK -> {
                if(mInitStateView.getNoNetworkId() == 0){
                    mViewReplacer.replace(mInitStateView.getNoNetworkView())
                }else {
                    mViewReplacer.replace(mInitStateView.getNoNetworkId())
                }
            }
            STATE_EMPTY -> {
                if(mInitStateView.getEmptyId() == 0){
                    mViewReplacer.replace(mInitStateView.getEmptyView())
                }else {
                    mViewReplacer.replace(mInitStateView.getEmptyId())
                }
            }
            STATE_CONTENT -> {
                mViewReplacer.restore()
            }
        }
    }

    fun setOnClick(listener: View.OnClickListener?){
        mViewReplacer.currentView.setOnClickListener(listener)
    }


    fun getStateView():StateView{
        return mInitStateView.getStateView()
    }


    interface InitStateView{
        fun getLoadingId(): Int
        fun getErrorId(): Int
        fun getNoNetworkId(): Int
        fun getEmptyId(): Int

        fun getLoadingView(): View
        fun getErrorView(): View
        fun getNoNetworkView(): View
        fun getEmptyView(): View

        fun getStateView():StateView
    }

}