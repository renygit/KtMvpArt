package com.git.reny.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    private var canScroll = true

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (canScroll) {
            try {
                super.onInterceptTouchEvent(ev)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                false
            }
        } else false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (canScroll) {
            super.onTouchEvent(event)
        } else false
    }

    fun toggleLock() {
        canScroll = !canScroll
    }

    fun setCanScroll(canScroll: Boolean) {
        this.canScroll = canScroll
    }

    fun isCanScroll(): Boolean {
        return canScroll
    }
}