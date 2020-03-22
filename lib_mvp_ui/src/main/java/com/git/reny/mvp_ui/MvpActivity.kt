package com.git.reny.mvp_ui

import android.os.Bundle
import android.view.View
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.ViewReplacer
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.git.reny.lib_base.base.*
import com.git.reny.mvp.MvpBaseActivity
import com.scwang.smart.refresh.layout.api.RefreshLayout

abstract class MvpActivity : MvpBaseActivity() {

    protected var screenBuilder : ViewSkeletonScreen.Builder? = null
    private val mAllScreenBuilders = HashSet<ViewSkeletonScreen.Builder>()
    private val mAllScreens = HashSet<ViewSkeletonScreen>()

    protected var screenRecyclerBuilder: RecyclerViewSkeletonScreen.Builder? = null
    private val mAllScreenRecyclerBuilders = HashSet<RecyclerViewSkeletonScreen.Builder>()
    private val mAllScreenRecyclers = HashSet<RecyclerViewSkeletonScreen>()

    private var mViewReplacer:ViewReplacer? = null
    private var mStateView:StateView? = null
    private var mLoadingView:LoadingView? = null
    private var mRefreshLayout: RefreshLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addScreenBuilders()
        initRefreshLayout()
    }

    private fun addScreenBuilders() {
        getScreenBuilders()?.forEach {
            mAllScreenBuilders.add(it)
        }
        getScreenRecyclerBuilders()?.forEach {
            mAllScreenRecyclerBuilders.add(it)
        }
    }


    //多个ViewSkeletonScreen.Builder 重写
    open fun getScreenBuilders():MutableList<ViewSkeletonScreen.Builder>?{
        screenBuilder?.let {
            return mutableListOf(it)
        }
        return null
    }

    //多个RecyclerViewSkeletonScreen.Builder 重写
    open fun getScreenRecyclerBuilders():MutableList<RecyclerViewSkeletonScreen.Builder>?{
        screenRecyclerBuilder?.let {
            return mutableListOf(it)
        }
        return null
    }

    open fun getViewReplacer() : ViewReplacer?{
        if(mViewReplacer == null) {
            val view: View? = window.decorView.findViewById(R.id.viewReplacer)
            view?.let {
                mViewReplacer = ViewReplacer(it)
            }
        }
        return mViewReplacer
    }

    //如果有刷新控件 需要提前初始化好 和上面getViewReplacer()不太一样
    open fun initRefreshLayout(){
        if(mRefreshLayout == null) {
            val view: View? = window.decorView.findViewById(R.id.srl)
            view?.let {
                if (view is RefreshLayout) {
                    mRefreshLayout = view
                    //init
                    mRefreshLayout!!.setEnableAutoLoadMore(true) //开启自动加载功能（非必须）
                    mRefreshLayout!!.setEnableHeaderTranslationContent(true) //内容跟随下拉
                    mRefreshLayout!!.setEnableLoadMoreWhenContentNotFull(true) //是否在列表不满一页时候开启上拉加载功能
                    mRefreshLayout!!.setEnableFooterFollowWhenNoMoreData(true) //设置是否在没有更多数据之后 Footer 跟随内容

                    mRefreshLayout!!.setOnRefreshListener {
                        mAllPresenters.forEach { it.loadData(true) }
                    }

                    mRefreshLayout!!.setOnLoadMoreListener {
                        mAllPresenters.forEach { it.loadData(false) }
                    }
                }
            }
        }
    }

    open fun getLoadingId(): Int = 0
    open fun getErrorId(): Int = 0
    open fun getNoNetworkId(): Int = 0
    open fun getEmptyId(): Int = 0


    open fun getLoadingView(): View{
        if(mLoadingView == null){
            mLoadingView = LoadingView(this)
        }
        return mLoadingView!!
    }

    //不想自定义错误页面 但想修改文字，图片 可以重写此方法
    open fun getErrorView(): View{
        getStateView().setImg(R.mipmap.ic_error)
        getStateView().setTip("加载出错，请重试")
        return getStateView()
    }
    //不想自定义错误页面 但想修改文字，图片 可以重写此方法
    open fun getNoNetworkView(): View{
        getStateView().setImg(R.mipmap.ic_error)
        getStateView().setTip("网络链接失败，请重试")
        return getStateView()
    }
    //不想自定义错误页面 但想修改文字，图片 可以重写此方法
    open fun getEmptyView(): View{
        getStateView().setImg(R.mipmap.ic_empty)
        getStateView().setTip("暂无相关数据")
        return getStateView()
    }

    protected fun getStateView():StateView{
        if(mStateView == null){
            mStateView = StateView(this)
        }
        return mStateView!!
    }


    override fun showLoading(@LoadState state: Int) {
        when(state){
            STATE_LOADING -> {
                //如果Loading为骨架屏
                if(mAllScreenBuilders.size > 0 || mAllScreenRecyclerBuilders.size > 0){
                    if(mAllScreenBuilders.size > 0) {
                        mAllScreens.clear()
                        mAllScreenBuilders.forEach {
                            mAllScreens.add(it.show())
                        }
                    }
                    if(mAllScreenRecyclerBuilders.size > 0){
                        mAllScreenRecyclers.clear()
                        mAllScreenRecyclerBuilders.forEach {
                            mAllScreenRecyclers.add(it.show())
                        }
                    }
                } else {
                    //如果Loading为一个普通布局文件
                    useViewReplacer(state)
                }
            }
            STATE_CONTENT -> {
                if(mAllScreens.size > 0){
                    mAllScreens.forEach {
                        it.hide()
                    }
                }
                if(mAllScreenRecyclers.size > 0){
                    mAllScreenRecyclers.forEach {
                        it.hide()
                    }
                }
                useViewReplacer(state)
            }
            else -> {
                //加载出错，网络出错，数据为空等页面 依然采用普通布局文件
                useViewReplacer(state)
            }

        }
    }

    private fun useViewReplacer(@LoadState state: Int){
        val viewReplacer:ViewReplacer? = getViewReplacer()
        if(viewReplacer != null){
            when(state){
                STATE_LOADING -> {
                    if(getLoadingId() == 0){
                        viewReplacer.replace(getLoadingView())
                    }else {
                        viewReplacer.replace(getLoadingId())
                    }
                }
                STATE_ERROR -> {
                    if(getErrorId() == 0){
                        viewReplacer.replace(getErrorView())
                    }else {
                        viewReplacer.replace(getErrorId())
                    }
                }
                STATE_NO_NETWORK -> {
                    if(getNoNetworkId() == 0){
                        viewReplacer.replace(getNoNetworkView())
                    }else {
                        viewReplacer.replace(getNoNetworkId())
                    }
                }
                STATE_EMPTY -> {
                    if(getEmptyId() == 0){
                        viewReplacer.replace(getEmptyView())
                    }else {
                        viewReplacer.replace(getEmptyId())
                    }
                }
                STATE_CONTENT -> {
                    viewReplacer.restore()
                }
            }

            viewReplacer.sourceView.setOnClickListener(if(state == STATE_LOADING || state == STATE_CONTENT) null else View.OnClickListener {
                //点击重试
                mAllPresenters.forEach { it.loadData(true) }
            })
        }else{
            val loadingTip = if(state == STATE_LOADING) "需要重写screenBuilder 或者 " else ""
            throw RuntimeException("${this.javaClass.simpleName}>>$loadingTip 需要在xml定义一个id名为viewReplacer的控件显示 $state")
        }
    }



    override fun showRefresh(@RefreshState state: Int) {
        if(mRefreshLayout != null){
            when(state){
                STATE_REFRESH_HIDE -> {//刷新完成回调
                    mRefreshLayout!!.finishRefresh()
                    mRefreshLayout!!.resetNoMoreData()
                }
                STATE_LOAD_MORE_HIDE_HAS_MORE -> {//上拉加载更多回调
                    mRefreshLayout!!.finishLoadMore()
                }
                STATE_LOAD_MORE_HIDE_NO_MORE -> {
                    mRefreshLayout!!.finishLoadMoreWithNoMoreData()
                }
                else -> {
                    //其它情况：
                    //下拉刷新不用代码控制了 除非有调用loadData就自动显示下拉刷新的需求 控件本身也支持这种 所以不处理
                }
            }
        }else{
            throw RuntimeException("${this.javaClass.simpleName}>> 需要在xml定义一个id名为srl的SmartRefreshLayout控件，用于刷新")
        }
    }
}