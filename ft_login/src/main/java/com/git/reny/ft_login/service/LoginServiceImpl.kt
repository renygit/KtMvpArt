package com.git.reny.ft_login.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.git.reny.ft_login.manager.UserManager
import com.git.reny.lib_base.config.RConfig
import com.git.reny.lib_base.ft_login.model.User
import com.git.reny.lib_base.ft_login.service.LoginService

@Route(path = RConfig.FtLogin.login_service)
class LoginServiceImpl : LoginService {

    override fun getUser(): User? = UserManager.user

    override fun isLogin(): Boolean = UserManager.isLogin()

    override fun init(context: Context?) {
        //loge("LoginServiceImpl init")
    }
}