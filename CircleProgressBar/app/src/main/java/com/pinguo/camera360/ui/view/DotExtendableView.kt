package com.pinguo.camera360.ui.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Build
import android.support.v4.widget.ScrollerCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * <pre>
 *     author : zhaoshuanghe
 *     time   : 2018/07/10
 * </pre>
 */
class DotExtendableView : View {

    private var mExtended: Boolean = false
    private var mCurrentLength: Int = 0
    private val ANIMATION_DURATION = 300
    private var mQuarterWidth = 0f
    private var mQuarterHeight = 0f
    private var mHalfWidth = 0f
    private var mOneSevenWidth = 0f
    private var mHeight = 0f
    private var mScroller: ScrollerCompat
    private var mColor: Int = Color.parseColor("#FFFFFF")
    private var linePaint: Paint

    private var mAccelerateDecelerateInterpolator: AccelerateDecelerateInterpolator

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        if (ApiHelper.AFTER_HONEYCOMB) {
        closeHardwareAccelerated()
//        }
        mAccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
        mScroller = ScrollerCompat.create(context, mAccelerateDecelerateInterpolator)
        linePaint = Paint()

        linePaint.strokeWidth = 6f
        linePaint.color = mColor
        linePaint.style = Paint.Style.FILL
        linePaint.isAntiAlias = true
        linePaint.isDither = true//防抖动
        linePaint.strokeCap = Paint.Cap.ROUND//圆形线冒
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mQuarterWidth = width.toFloat() / 4
        mQuarterHeight = height.toFloat() / 4
        mHalfWidth = width.toFloat() / 2
        mOneSevenWidth = width.toFloat() / 7
        mHeight = height.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        if (mScroller!!.computeScrollOffset()) {
            mCurrentLength = mScroller.currX
            invalidate()
        }

        if (!mExtended) {
            if (mCurrentLength == 0) {
                canvas?.drawCircle(mHalfWidth, mQuarterHeight, 4f, linePaint)
                canvas?.drawCircle(mHalfWidth, mQuarterHeight * 2, 4f, linePaint)
                canvas?.drawCircle(mHalfWidth, mQuarterHeight * 3, 4f, linePaint)
            } else {
                canvas?.drawLine(mHalfWidth - mCurrentLength / 7 * 2, mQuarterHeight, mHalfWidth + mCurrentLength / 7 * 2, mQuarterHeight, linePaint)
                canvas?.drawLine(mHalfWidth - mCurrentLength / 2, mQuarterHeight * 2, mHalfWidth + mCurrentLength / 2, mQuarterHeight * 2, linePaint)
                canvas?.drawLine(mHalfWidth - mCurrentLength / 7 * 2, mQuarterHeight * 3, mHalfWidth + mCurrentLength / 7 * 2, mQuarterHeight * 3, linePaint)
            }
        } else {
            if (mCurrentLength == (mQuarterWidth * 3).toInt()) {
                canvas?.drawCircle(mHalfWidth, height.toFloat() / 4, 4f, linePaint)
                canvas?.drawCircle(mHalfWidth, height.toFloat() / 4 * 2, 4f, linePaint)
                canvas?.drawCircle(mHalfWidth, height.toFloat() / 4 * 3, 4f, linePaint)
            } else {
                canvas?.drawLine(mOneSevenWidth * 2 + mCurrentLength * 2 / 7, height.toFloat() / 4, mOneSevenWidth * 5 - mCurrentLength * 2 / 7, mQuarterHeight, linePaint)
                canvas?.drawLine(mQuarterWidth / 2 + mCurrentLength / 2, height.toFloat() * 2 / 4, mQuarterWidth * 7 / 2 - mCurrentLength / 2, mQuarterHeight * 2, linePaint)
                canvas?.drawLine(mOneSevenWidth * 2 + mCurrentLength * 2 / 7, height.toFloat() * 3 / 4, mOneSevenWidth * 5 - mCurrentLength * 2 / 7, mQuarterHeight * 3, linePaint)
            }
        }
    }

    fun set(color: Int) {
        linePaint.color = color
    }

    override fun dispatchDraw(canvas: Canvas?) {
        linePaint.setShadowLayer(2f, 0f, 0f, getDarkerColor(Color.parseColor("#33000000")))
        canvas?.save()
        super.dispatchDraw(canvas)
    }

    fun setExtended(extended: Boolean) {
        mExtended = extended
        startScroll()
    }

    fun isExtended(): Boolean {
        return mExtended
    }

    fun getDarkerColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[1] = hsv[1] + 0.1f
        hsv[2] = hsv[2] - 0.1f
        val darkerColor = Color.HSVToColor(hsv)
        return darkerColor
    }

    private fun startScroll() {
        mScroller?.startScroll(0, 0, (mQuarterWidth * 3).toInt(), 0, ANIMATION_DURATION)
        invalidate()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun closeHardwareAccelerated() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }
}