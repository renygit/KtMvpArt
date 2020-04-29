package com.git.reny.ft_login

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.git.reny.ft_login.manager.UserManager
import com.git.reny.lib_base.base.extras.onClick
import com.git.reny.lib_base.base.extras.singleClick
import com.git.reny.lib_base.config.RConfig
import com.git.reny.lib_base.ft_login.model.User
import com.git.reny.mvp_ui.MvpActivity
import kotlinx.android.synthetic.main.activity_login__main.*

@Route(path = RConfig.FtLogin.login)
class LoginMainActivity : MvpActivity() {

    private val btnLogin by lazy { btn_login }

    override fun getLayoutId(): Int = R.layout.activity_login__main

    override fun initView(savedInstanceState: Bundle?) {
        btnLogin.onClick {
            singleClick {
                /*if(UserManager.isLogin()){
                    showToast("早已经登录")
                }*/
                UserManager.user = User("reny", "13648651135")
                //showToast("设置了user>> ${UserManager.user!!.userName}  ${UserManager.isLogin()}")
                //getARouter().build(RConfig.FtHome.home).navigation()
                finish()
            }
        }
    }
}
