package com.git.reny.common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import com.git.reny.common.R

/*
    加载对话框封装
 */
class ProgressLoading private constructor(context: Context, theme: Int) : Dialog(context, theme) {

    companion object {
        private lateinit var mDialog: ProgressLoading

        /*
            创建加载对话框
         */
        fun create(context: Context):ProgressLoading {
            //样式引入
            mDialog = ProgressLoading(context, R.style.LightProgressDialog)
            //设置布局
            mDialog.setContentView(R.layout.progress_dialog)
            mDialog.setCancelable(true)
            mDialog.setCanceledOnTouchOutside(false)
            mDialog.window?.attributes?.gravity = Gravity.CENTER

            val lp = mDialog.window?.attributes
            lp?.dimAmount = 0.2f
            //设置属性
            mDialog.window?.attributes = lp

            return mDialog
        }
    }
}
