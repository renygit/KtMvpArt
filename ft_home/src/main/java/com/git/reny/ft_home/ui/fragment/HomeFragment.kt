package com.git.reny.ft_home.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.ethanhua.skeleton.Skeleton
import com.git.reny.ft_home.R
import com.git.reny.ft_home.model.Girl
import com.git.reny.ft_home.mvp.HomeView
import com.git.reny.ft_home.presenter.HomePresenter
import com.git.reny.ft_home.ui.adapter.BannerAdapter
import com.git.reny.ft_home.ui.adapter.HomeListAdapter
import com.git.reny.lib_base.base.extras.business.checkLogin
import com.git.reny.lib_base.base.extras.showToast
import com.git.reny.lib_base.base.extras.singleClick
import com.git.reny.lib_base.ft_login.service.LoginApi
import com.git.reny.mvp_ui.MvpFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : MvpFragment(), HomeView {

    override val mPresenter: HomePresenter = HomePresenter(this)

    override fun getLayoutId(): Int = R.layout.fragment_home


    private val recycler:RecyclerView by lazy { rv }

    private lateinit var layoutManager:VirtualLayoutManager
    private lateinit var adapter: DelegateAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var homeListAdapter: HomeListAdapter

    override fun initView(savedInstanceState: Bundle?) {
        layoutManager = VirtualLayoutManager(requireContext())
        recycler.layoutManager = layoutManager
        adapter = DelegateAdapter(layoutManager, true)

        val viewPool = RecyclerView.RecycledViewPool()
        recycler.setRecycledViewPool(viewPool)

        bannerAdapter = BannerAdapter()
        homeListAdapter = HomeListAdapter()

        viewPool.setMaxRecycledViews(bannerAdapter.getItemViewType(0), 20)
        viewPool.setMaxRecycledViews(homeListAdapter.getItemViewType(0), 20)

        adapter.addAdapter(bannerAdapter)
        adapter.addAdapter(homeListAdapter)

        recycler.adapter = adapter

        homeListAdapter.setOnItemClickListener { _, _, position ->
            singleClick { checkLogin { showToast("已经登录 ${LoginApi.api.getUser()?.userName} 点击位置$position") }}
        }

        mScreenRecyclerBuilder = Skeleton.bind(recycler)
            .adapter(adapter)
            .count(1)
            .load(R.layout.screen_home)
    }

    override fun lazyLoad() {
        mPresenter.loadData(true)
    }

    override fun setResult(resultBanner: List<Girl>?, resultList: List<Girl>?, isRefresh: Boolean) {
        resultBanner?.let {
            bannerAdapter.setNewData(mutableListOf(resultBanner.subList(0, 5)))
        }
        resultList?.let {
            if(isRefresh) homeListAdapter.setNewData(resultList.toMutableList()) else homeListAdapter.addData(resultList)
        }
    }
}