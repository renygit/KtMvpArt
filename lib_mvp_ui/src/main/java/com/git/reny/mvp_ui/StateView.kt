package com.git.reny.mvp_ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class StateView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var stateView: View = LayoutInflater.from(context).inflate(R.layout.view_state_simple, this)
    private var iv_state_img:ImageView
    private var tv_state_tip:TextView

    init {
        iv_state_img = stateView.findViewById(R.id.iv_state_img)
        tv_state_tip = stateView.findViewById(R.id.tv_state_tip)
    }

    fun setImg(imgId:Int){
        iv_state_img.setImageResource(imgId)
    }

    fun setTip(tip:String){
        tv_state_tip.text = tip
    }


}