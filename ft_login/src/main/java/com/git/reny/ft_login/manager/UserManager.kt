package com.git.reny.ft_login.manager

import com.git.reny.lib_base.ft_login.model.User

object UserManager {

    var user:User ? = null

    fun isLogin():Boolean = user != null

}