package com.git.reny.ktapp

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.git.reny.lib_base.base.RBaseActivity
import com.git.reny.lib_base.base.extras.getARouter
import com.git.reny.lib_base.config.RConfig
import kotlinx.android.synthetic.main.activity_splash.*

@Route(path = RConfig.App.splash)
class SplashActivity : RBaseActivity(), ViewPropertyAnimatorListener {

    private val flRoot by lazy {fl_root}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ViewCompat.animate(flRoot).scaleX(1.0f).scaleY(1.0f)
            .setListener(this).duration = 2000
    }

    override fun isTransStatusBar(): Boolean = true

    override fun onAnimationEnd(view: View?) {
        getARouter().build(RConfig.FtHome.home)
            .withString("name", "Splash haha")
            .navigation()
        finish()
    }

    override fun onAnimationCancel(view: View?) {}

    override fun onAnimationStart(view: View?) {}
}
