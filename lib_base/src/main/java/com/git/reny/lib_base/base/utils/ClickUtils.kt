package com.git.reny.lib_base.base.utils

class ClickUtils {

    companion object{

        private var lastClickTime: Long = 0

        fun isFastClick() : Boolean{
            val time = System.currentTimeMillis()
            if (time - lastClickTime < 500) {
                return true
            }
            lastClickTime = time
            return false
        }

    }

}