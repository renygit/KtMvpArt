package com.git.reny.lib_base.config

class RConfig {

    class App{
        companion object {
            const val splash = "/app/SplashActivity"
        }
    }

    class FtLogin{
        companion object {
            private const val module = "/ft_login/"
            const val login = "${module}LoginMainActivity"

            const val login_service = "${module}login_service"
        }
    }

    class FtHome{
        companion object {
            private const val module = "/ft_home/"
            const val home = "${module}HomeMainActivity"
        }
    }



}