package com.git.reny.ft_home.mvp

import com.git.reny.ft_home.model.Girl
import com.git.reny.mvp_ui.MvpView

interface HomeView : MvpView {

    fun setResult(resultBanner: List<Girl>?, resultList: List<Girl>?, isRefresh: Boolean)

}