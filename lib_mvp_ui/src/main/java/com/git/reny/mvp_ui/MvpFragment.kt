package com.git.reny.mvp_ui

import android.os.Bundle
import android.view.View
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.git.reny.common.widget.ProgressLoading
import com.git.reny.lib_base.base.*
import com.git.reny.lib_base.base.extras.setStatusHeight
import com.git.reny.mvp.MvpBaseFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout

abstract class MvpFragment : MvpBaseFragment(), MvpView {

    protected var mScreenBuilder : ViewSkeletonScreen.Builder? = null
    private val mAllScreenBuilders = HashSet<ViewSkeletonScreen.Builder>()
    private val mAllScreens = HashSet<ViewSkeletonScreen>()

    protected var mScreenRecyclerBuilder: RecyclerViewSkeletonScreen.Builder? = null
    private val mAllScreenRecyclerBuilders = HashSet<RecyclerViewSkeletonScreen.Builder>()
    private val mAllScreenRecyclers = HashSet<RecyclerViewSkeletonScreen>()

    protected var mMsv: MultiStateView? = null
    private var mRefreshLayout: RefreshLayout? = null

    private var mProgressLoading: ProgressLoading? = null

    //private var loadState: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStatusView()
        initMultiStateView()
        addScreenBuilders()
        initRefreshLayout()
    }

    private fun initStatusView() {
        val status: View? = rootView?.findViewById(R.id.status)
        status?.let {
            setStatusHeight(it)
        }
    }

    private fun initMultiStateView() {
        val view: View? = rootView?.findViewById(R.id.msv)
        view?.let {
            mMsv = MultiStateView(it)
        }
    }

    override fun getStateView(): StateView? {
        return mMsv?.getStateView()
    }

    override fun initView(savedInstanceState: Bundle?) {

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
        mScreenBuilder?.let {
            return mutableListOf(it)
        }
        return null
    }

    //多个RecyclerViewSkeletonScreen.Builder 重写
    open fun getScreenRecyclerBuilders():MutableList<RecyclerViewSkeletonScreen.Builder>?{
        mScreenRecyclerBuilder?.let {
            return mutableListOf(it)
        }
        return null
    }

    //如果有刷新控件 需要提前初始化好 和上面getViewReplacer()不太一样
    open fun initRefreshLayout(){
        if(mRefreshLayout == null) {
            val view: View? = rootView?.findViewById(R.id.srl)
            view?.let {
                if (view is RefreshLayout) {
                    mRefreshLayout = view
                    //init
                    mRefreshLayout!!.setEnableAutoLoadMore(true) //开启自动加载功能（非必须）
                    mRefreshLayout!!.setEnableHeaderTranslationContent(true) //内容跟随下拉
                    mRefreshLayout!!.setEnableLoadMoreWhenContentNotFull(true) //是否在列表不满一页时候开启上拉加载功能
                    mRefreshLayout!!.setEnableFooterFollowWhenNoMoreData(true) //设置是否在没有更多数据之后 Footer 跟随内容

                    mRefreshLayout!!.setOnRefreshListener {
                        //只关联一个Presenter 如果有多个Presenter 细节自主处理
                        mPresenter?.loadData(true)
                        //mAllPresenters.forEach { it.loadData(true) }
                    }

                    mRefreshLayout!!.setOnLoadMoreListener {
                        //只关联一个Presenter 如果有多个Presenter 细节自主处理
                        mPresenter?.loadData(false)
                        //mAllPresenters.forEach { it.loadData(false) }
                    }
                }
            }
        }
    }


    override fun showPageState(@LoadState state: Int) {
        //loadState = state
        when(state){
            STATE_LOADING -> {
                //如果Loading为骨架屏
                if(mAllScreenBuilders.size > 0 || mAllScreenRecyclerBuilders.size > 0){
                    mMsv?.showContent()
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
        mMsv?.showState(state)
        mMsv?.setOnClick(if(state == STATE_LOADING || state == STATE_CONTENT) null else View.OnClickListener {
            //点击重试 只关联一个Presenter 如果有多个Presenter 细节自主处理
            mPresenter?.loadData(true)
            //mAllPresenters.forEach { it.loadData(true) }
        })
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


    override fun showLoading() {
        if(mProgressLoading == null){
            mProgressLoading = ProgressLoading.create(requireContext())
        }
        mProgressLoading?.show()
    }

    override fun hideLoading() {
        mProgressLoading?.dismiss()
    }


    /*override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(loadState != 0 && loadState != STATE_CONTENT){
            loge("onSaveInstanceState loadState>> $loadState")
            outState.putInt("loadState", loadState)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("loadState")){
                loge("onViewStateRestored loadState>> $loadState")
                loadState = savedInstanceState.getInt("loadState")
                showLoading(loadState)
            }
        }
    }*/
}