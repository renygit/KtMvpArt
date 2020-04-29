package com.git.reny.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.git.reny.lib_base.base.RBaseFragment

/**
 * Created by reny on 2020/03/19.
 *  懒加载见：https://blog.csdn.net/qq_36486247/article/details/102531304
 * //构造 FragmentStatePagerAdapter 时传入Behavior：BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT  即可实现懒加载
 */
abstract class MvpBaseFragment : RBaseFragment(), MvpBaseView{

    override val mPresenter: MvpBasePresenter<out MvpBaseView>? = null
    protected val mAllPresenters = HashSet<MvpBasePresenter<*>>()

    protected var rootView:View? = null
    private var isFirstLoadPage = true // 是否第一次加载页面
    private var isViewCreated = false //是否创建好view
    private var isVisibleToMe = false //是否可见

    open fun isLazyLoad():Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false)
        } else{
            try {
                rootView?.parent?.let {
                    it as ViewGroup
                    it.removeView(rootView)
                }
            }catch (e:Exception){e.printStackTrace()}
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        initBeforeLazyLoad(savedInstanceState)
    }

    private fun initBeforeLazyLoad(savedInstanceState: Bundle?){
        if(mAllPresenters.size > 0){
            mAllPresenters.clear()
        }
        addPresenters()
        mAllPresenters.forEach { it.onCreateView(savedInstanceState) }
        initView(savedInstanceState)
    }

    abstract fun getLayoutId(): Int

    abstract fun lazyLoad()

    override fun finish() {
        activity?.finish()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisibleToMe = isVisibleToUser
        tempLoad()
    }
    /*override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }*/

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

        tempLoad()
        mAllPresenters.forEach { it.onResume() }
    }

    private fun tempLoad(){
        if(isFirstLoadPage && isLazyLoad() && isVisibleToMe && isViewCreated){
            lazyLoad()
            isFirstLoadPage = false
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        mAllPresenters.forEach { it.onDestroyView() }
    }

}