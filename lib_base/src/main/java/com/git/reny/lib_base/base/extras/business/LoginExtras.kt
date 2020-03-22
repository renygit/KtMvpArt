package com.git.reny.lib_base.base.extras.business

import com.git.reny.lib_base.base.extras.getARouter
import com.git.reny.lib_base.base.extras.showToast
import com.git.reny.lib_base.config.RConfig
import com.git.reny.lib_base.ft_login.service.LoginApi

fun Any.checkLogin(tip:String = "您未登录，请先登录", nextFunc: () -> Unit){
    if(LoginApi.api.isLogin()){
        nextFunc()
    }else {
        showToast(tip)
        getARouter().build(RConfig.FtLogin.login).navigation()
    }
}