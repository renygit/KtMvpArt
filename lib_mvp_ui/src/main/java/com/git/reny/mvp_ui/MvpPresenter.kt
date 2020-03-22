package com.git.reny.mvp_ui

import com.git.reny.mvp.MvpBasePresenter

abstract class MvpPresenter<V : MvpView>(v: V) : MvpBasePresenter<V>(v) {

}