package com.git.reny.mvp

import android.app.Activity
import android.os.Bundle
import com.git.reny.lib_base.base.RBaseActivity
import java.lang.ref.WeakReference

abstract class MvpBaseActivity : RBaseActivity(), MvpBaseView{

    override val mPresenter: MvpBasePresenter<out MvpBaseView>? = null
    protected val mAllPresenters = HashSet<MvpBasePresenter<*>>()
    private var mActivity: WeakReference<Activity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        if(mAllPresenters.size > 0){
            mAllPresenters.clear()
        }
        addPresenters()
        mAllPresenters.forEach { it.onCreate(savedInstanceState) }
        initView(savedInstanceState)
    }

    abstract fun getLayoutId(): Int

    override fun getActivity(): Activity? {
        if(mActivity == null){
            mActivity = WeakReference(this)
        }
        return mActivity?.get()
    }

    override fun finish() {
        super.finish()
    }

    //如果有多个presenter 就重写该方法getPresenters()， 只一个就重写如下
    //override val mPresenter: YourPresenter= YourPresenter(this)
    open fun getPresenters():MutableList<MvpBasePresenter<*>>{
        return if(mPresenter == null) mutableListOf() else mutableListOf(mPresenter!!)
    }

    private fun addPresenters() {
        getPresenters().forEach { mAllPresenters.add(it)}
    }

    override fun onStart() {
        super.onStart()
        mAllPresenters.forEach { it.onStart() }
    }

    override fun onResume() {
        super.onResume()
        mAllPresenters.forEach { it.onResume() }
    }

    override fun onPause() {
        super.onPause()
        mAllPresenters.forEach { it.onPause() }
    }

    override fun onStop() {
        super.onStop()
        mAllPresenters.forEach { it.onStop() }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAllPresenters.forEach { it.onDestroy() }
    }

}