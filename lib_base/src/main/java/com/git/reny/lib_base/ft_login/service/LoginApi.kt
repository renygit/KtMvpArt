package com.git.reny.lib_base.ft_login.service

import com.git.reny.lib_base.base.extras.getARouter
import com.git.reny.lib_base.config.RConfig

object LoginApi {

    //通过注册的path(RConfig.FtLogin.login_service) 获取到一个LoginService对应path的实现类
    val api = getARouter().build(RConfig.FtLogin.login_service).navigation() as LoginService

}