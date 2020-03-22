package com.git.reny.mvp

import android.app.Activity
import android.os.Bundle
import com.git.reny.lib_base.base.LoadState
import com.git.reny.lib_base.base.RefreshState

interface MvpBaseView {

    val mPresenter: MvpBasePresenter<out MvpBaseView>?

    fun getActivity() : Activity?

    fun finish()

    fun initView(savedInstanceState: Bundle?)

    //根据LoadState显示loading、error、noNetwork、empty、content
    fun showLoading(@LoadState state: Int)

    //根据RefreshState显示refresh_show、refresh_hide、load_more_show、load_more_hide
    fun showRefresh(@RefreshState state: Int)

}