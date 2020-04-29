package com.git.reny.mvp

import android.app.Activity
import android.os.Bundle
import com.git.reny.lib_base.base.*
import java.lang.ref.SoftReference
import java.util.concurrent.Future

abstract class MvpBasePresenter<V : MvpBaseView>(v: V) {

    /***
     * SoftReference 软引用所指向的对象要进行回收，需要满足两个条件：
        1. 没有任何强引用 指向 软引用指向的对象(内存中的Person对象)
        2. JVM需要内存时，即在抛出OOM之前
        (WeakReference 弱引用只用满足第1条就回收)
     */
    open var mView: SoftReference<V> = SoftReference(v)

    fun getIView():V{
        mView.get()?.let {
            return it
        }
        throw RuntimeException("activity 已经被回收，无法在Presenter调用")
    }

    fun getActivity():Activity?{
        return getIView().getActivity()
    }

    open fun onCreate(savedInstanceState: Bundle?) {}
    open fun onStart() {}
    open fun onResume() {}
    open fun onPause() {}
    open fun onStop() {}

    //对anko协程任务的管理
    private val mJobs = HashSet<Future<Unit>>()
    fun addJob(job: Future<Unit>):Future<Unit>{
        if(!mJobs.contains(job)) {
            mJobs.add(job)
        }
        return job
    }
    open fun onDestroy() {
        mJobs.forEach {
            try {
                it.cancel(true)
            }catch (e:Exception){e.printStackTrace()}
        }
        mJobs.clear()
    }
    open fun onCreateView(arguments: Bundle?) {}
    open fun onDestroyView() {}

    //如果是第一次进入页面
    open var isFirst:Boolean = true

    //重写时别忘记调用super.loadData
    open fun loadData(isRefresh:Boolean){
        if(isFirst){
            //如果是第一次进入页面 可以展示LOADING页面
            getIView().showPageState(STATE_LOADING)
        }else{
            //加载过 就认为是在执行下拉刷新 或上拉加载更多(多半用不到，下拉上拉一般是自动变化的UI，不用代码设置)
            getIView().showRefresh(if(isRefresh) STATE_REFRESH_SHOW else STATE_LOAD_MORE_SHOW)
        }
    }

    //数据加载完成后应该调用的方法 设置相关UI状态
    open fun setLoadState(@LoadState state: Int, hasMore:Boolean, isRefresh:Boolean){
        getIView().hideLoading()
        if(isFirst){
            getIView().showPageState(state)
            if(state == STATE_CONTENT){
                isFirst = false
            }
        }else{
            //回调去关闭下拉上拉UI状态
            getIView().showRefresh(if(isRefresh) STATE_REFRESH_HIDE else if(hasMore) STATE_LOAD_MORE_HIDE_HAS_MORE else STATE_LOAD_MORE_HIDE_NO_MORE)
        }
    }


}