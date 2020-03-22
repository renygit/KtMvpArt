package com.git.reny.ft_home.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.git.reny.ft_home.R
import com.git.reny.ft_home.ui.adapter.TabPagerAdapter
import com.git.reny.ft_home.ui.fragment.HomeFragment
import com.git.reny.lib_base.config.RConfig
import com.git.reny.mvp_ui.MvpActivity
import kotlinx.android.synthetic.main.activity_home_main.*
import me.majiajie.pagerbottomtabstrip.NavigationController


@Route(path = RConfig.FtHome.home)
class HomeMainActivity : MvpActivity() {

    //ARouter 参数传递
    @JvmField
    @Autowired
    var name: String? = null

    private val viewPager by lazy {vp}
    private val tabNavigation by lazy {tab}
    private var fragmentList: MutableList<Fragment>? = null

    override fun getLayoutId(): Int = R.layout.activity_home_main

    override fun initView(savedInstanceState: Bundle?) {
        //showToast(name)
        if(fragmentList == null) {
            fragmentList = mutableListOf()
            for (i in 0..3) {
                fragmentList!!.add(HomeFragment())
            }
        }
        //fragmentList.add(TestFragment())
        viewPager.setCanScroll(false)
        viewPager.offscreenPageLimit = 4
        viewPager.adapter = TabPagerAdapter(supportFragmentManager, fragmentList!!)

        val navigationController: NavigationController = tabNavigation.material()
            .addItem(android.R.drawable.ic_menu_camera, "相机")
            .addItem(android.R.drawable.ic_menu_compass, "位置")
            .addItem(android.R.drawable.ic_menu_search, "搜索")
            .addItem(android.R.drawable.ic_menu_help, "帮助")
            .build()
        navigationController.setupWithViewPager(viewPager);
    }

}
