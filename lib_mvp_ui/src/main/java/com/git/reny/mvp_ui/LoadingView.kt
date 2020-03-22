package com.git.reny.mvp_ui

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    View(context, attrs) {
    //旋转圆的画笔
    private var mPaint: Paint? = null
    //属性动画
    private var mValueAnimator: ValueAnimator? = null
    //背景色
    private val mBackgroundColor = Color.WHITE
    private var mCircleColors: IntArray
    //表示旋转圆的中心坐标
    private var mCenterX = 0f
    private var mCenterY = 0f
    //表示斜对角线长度的一半,扩散圆最大半径
    private var mDistance = 0f
    //6个小球的半径
    private val mCircleRadius = 18f
    //旋转大圆的半径
    private val mRotateRadius = 60f
    //当前大圆的旋转角度
    private var mCurrentRotateAngle = 0f
    //当前大圆的半径
    private var mCurrentRotateRadius = mRotateRadius
    //表示旋转动画的时长
    private val mRotateDuration = 1200

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCircleColors = context.resources.getIntArray(R.array.splash_circle_colors)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w * 1f / 2
        mCenterY = h * 1f / 2
        mDistance = (hypot(w.toDouble(), h.toDouble()) / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mState == null) {
            mState = RotateState()
        }
        mState!!.drawState(canvas)
    }

    private var mState: SplashState? = null

    private abstract inner class SplashState {
        abstract fun drawState(canvas: Canvas)
    }

    //1.旋转
    private inner class RotateState : SplashState() {

        override fun drawState(canvas: Canvas) { //绘制背景
            drawBackground(canvas)
            //绘制6个小球
            drawCircles(canvas)
        }

        init {
            mValueAnimator = ValueAnimator.ofFloat(0f, (Math.PI * 2).toFloat())
            mValueAnimator!!.repeatCount = -1
            mValueAnimator!!.duration = mRotateDuration.toLong()
            mValueAnimator!!.interpolator = LinearInterpolator()
            mValueAnimator!!.addUpdateListener(AnimatorUpdateListener { animation ->
                mCurrentRotateAngle = animation.animatedValue as Float
                invalidate()
            })
            mValueAnimator!!.start()
        }


    }

    private fun drawCircles(canvas: Canvas) {
        val rotateAngle = (Math.PI * 2 / mCircleColors.size).toFloat()
        for (i in mCircleColors.indices) {
            // x = r * cos(a) + centX;
            // y = r * sin(a) + centY;
            val angle = i * rotateAngle + mCurrentRotateAngle
            val cx =
                (cos(angle.toDouble()) * mCurrentRotateRadius + mCenterX).toFloat()
            val cy =
                (sin(angle.toDouble()) * mCurrentRotateRadius + mCenterY).toFloat()
            mPaint!!.color = mCircleColors[i]
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint!!)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(mBackgroundColor)
    }

}