package com.git.reny.lib_base.ft_login.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.git.reny.lib_base.ft_login.model.User


//登录模块对外暴露的接口

interface LoginService : IProvider {

    fun getUser(): User?

    fun isLogin(): Boolean

}