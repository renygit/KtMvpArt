package com.git.reny.mvp_ui

import com.git.reny.mvp.MvpBaseView

interface MvpView : MvpBaseView {

    fun getStateView():StateView

}