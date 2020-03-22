package com.git.reny.ft_home.presenter

import android.os.SystemClock
import com.git.reny.ft_home.api.Api
import com.git.reny.ft_home.mvp.HomeView
import com.git.reny.lib_base.base.STATE_CONTENT
import com.git.reny.lib_base.base.STATE_EMPTY
import com.git.reny.lib_base.base.extras.business.http.doRequest
import com.git.reny.lib_base.base.extras.isEmpty
import com.git.reny.mvp_ui.MvpPresenter
import com.git.reny.mvp_ui.extras.exceptionHandle
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class HomePresenter(v: HomeView) : MvpPresenter<HomeView>(v) {

    private var page = 1;

    override fun loadData(isRefresh: Boolean) {
        super.loadData(isRefresh)

        addJob(doAsync(exceptionHandle(), {
            if (isRefresh) page = 1
            val resultBanner = if(isRefresh) Api.api().get_imgs(page).doRequest() else null
            val resultList = Api.api().get_imgs(page + 1).doRequest()
            page++

            SystemClock.sleep(1000)//为了看到 骨架屏 的UI效果
            uiThread {
                val empty = isEmpty(resultBanner) && isEmpty(resultList)
                setLoadState(if(empty) STATE_EMPTY else STATE_CONTENT, !empty, isRefresh)
                getIView().setResult(resultBanner, resultList, isRefresh)
            }
        }))
    }

}