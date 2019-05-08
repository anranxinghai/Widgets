package com.pinguo.camera360.camera.view.focusView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import com.pinguo.camera360.R
import com.pinguo.camera360.lib.camera.view.PreviewConstants
import us.pinguo.foundation.utils.Util

class PGBlurView : View {

    private var radius = 0f
    private var linePaint: Paint = Paint()
    private var lineColor: Int = Color.WHITE

    private var blurState = PreviewConstants.BLUR_LINE
    private var screenWidth = 0
    private var screenHeight = 0
    private var diagonalLength = 0f
    private lateinit var transformLine: FloatArray
    private var defaultLineGap = 0
    private lateinit var defaultLine: FloatArray
    private var blurCicleDiameter = 0
    private var lastX = 0f
    private var lastY = 0f
    private var mMatrix = Matrix()
    private var invertMatrix = Matrix()
    private var lastLineDegrees = 0f
    private var lastLineScaleFactor = 1.0f
    private var touchPoint = FloatArray(2)
    private var inverTouchPoint = FloatArray(2)

    private lateinit var lineAnimInterpolator: Interpolator
    private var lineScaleAnimStartTime = 0L
    private var lintScaleAnimStartScale = 0f
    private val ANIM_STATE_NONE = 0
    private val ANIM_STATE_DOWN = 1
    private val ANIM_STATE_UP = 2
    private var animState = ANIM_STATE_NONE

    private val MIN_SCALE = 0.7f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.PGBlurView, defStyleAttr, 0)
        lineColor = ta.getColor(R.styleable.PGBlurView_lineColor, Color.WHITE)
        init()
    }

    fun init() {
        //画虚线需要关闭硬件加速
        setLayerType(LAYER_TYPE_HARDWARE, null)
        linePaint.strokeWidth = 1.5f
        linePaint.color = lineColor
        linePaint.isAntiAlias = true
        linePaint.style = Paint.Style.STROKE
        var effect = DashPathEffect(floatArrayOf(32f, 32f), 4f)
        linePaint.pathEffect = effect //设置虚线

        blurCicleDiameter = resources.getDimensionPixelSize(R.dimen.blur_circle_diameter)
        val activity: Activity = context as Activity
        val screenPoint = Point()
        activity.windowManager.defaultDisplay.getSize(screenPoint)
        screenWidth = screenPoint.x
        screenHeight = screenPoint.y

        diagonalLength = Math.sqrt(Math.pow(screenWidth.toDouble(), 2.0) + Math.pow(screenHeight.toDouble(), 2.0)).toFloat()
        defaultLineGap = resources.getDimensionPixelSize(R.dimen.focus_circle_radius) + Util.dpToPixel(20)
        transformLine = FloatArray(8)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            setMeasuredDimension(blurCicleDiameter, blurCicleDiameter)
            radius = (blurCicleDiameter / 2).toFloat()
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            lastY = (measuredHeight / 2).toFloat()
            updateDefaultLines()
        } else {
            setMeasuredDimension(0, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            canvas?.drawCircle((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), radius, linePaint)
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            mMatrix.mapPoints(transformLine, defaultLine)
            canvas?.drawLines(transformLine, linePaint)
            drawLineScaleAnimation()
        }
    }

    private fun drawLineScaleAnimation() {
        if (blurState == PreviewConstants.BLUR_LINE && animState != ANIM_STATE_NONE) {
            if (System.currentTimeMillis() <= lineScaleAnimStartTime + 300) {
                var animProgress = lineAnimInterpolator.getInterpolation((System.currentTimeMillis() - lineScaleAnimStartTime) / 300 * 1f)
                if (animState == ANIM_STATE_DOWN) {
                    lastLineScaleFactor = lintScaleAnimStartScale - (lintScaleAnimStartScale - MIN_SCALE) * animProgress
                } else {
                    lastLineScaleFactor = MIN_SCALE + (lintScaleAnimStartScale - MIN_SCALE) * animProgress
                }
                updateDefaultLines()
                invalidate()
                //动画时间已经结束，scaleFactor未达到指定值，做如下处理(边界容错处理)
            } else if (animState == ANIM_STATE_DOWN) {
                lastLineScaleFactor = MIN_SCALE
                updateDefaultLines()
                invalidate()
                animState = ANIM_STATE_NONE
            } else if (animState == ANIM_STATE_UP) {
                lastLineScaleFactor = lintScaleAnimStartScale
                updateDefaultLines()
                invalidate()
                animState = ANIM_STATE_NONE
            }
        }
    }

    private fun updateDefaultLines() {
        var lineGap = defaultLineGap * lastLineScaleFactor
        var widthOffset = (2 * diagonalLength - measuredWidth) / 2
        var line1X0 = -widthOffset
        var line1Y0 = lastY - lineGap / 2
        var line1X1 = line1X0 + 2 * diagonalLength
        var line1Y1 = line1Y0

        var line2X0 = line1X0
        var line2Y0 = line1Y0 + lineGap

        var line2X1 = line1X1
        var line2Y1 = line2Y0
        defaultLine = floatArrayOf(line1X0, line1Y0, line1X1, line1Y1, line2X0, line2Y0, line2X1, line2Y1)
    }

    fun updatePosition(x: Float, y: Float) {
        lastX = x
        lastY = y
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            var left = (x - measuredWidth / 2).toInt()
            var top = (y - measuredHeight / 2).toInt()
            var right = left + measuredWidth
            var bottom = top + measuredHeight
            layout(left, top, right, bottom)
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            mMatrix.reset()
            mMatrix.setRotate(lastLineDegrees, x, y)
            updateDefaultLines()
            invalidate()
        }
    }

    fun setScale(scaleFactor: Float) {
        if (animState == ANIM_STATE_NONE) {
            if (blurState == PreviewConstants.BLUR_LINE) {
                lastLineScaleFactor = scaleFactor
                updateDefaultLines()
                invalidate()
            } else if (blurState == PreviewConstants.BLUR_CIRCLE) {
                scaleX = scaleFactor
                scaleY = scaleFactor
                invalidate()
            }
        }
    }

    fun setRotate(degrees: Float) {
        lastLineDegrees = degrees
        mMatrix.reset()
        mMatrix.setRotate(degrees, lastX, lastY)
        invalidate()
    }

    fun setBlurState(state: Int) {
        if (state != blurState) {
            scaleX = 1.0f
            scaleY = 1.0f
            blurState = state
            requestLayout()
        }
    }

    fun reset() {
        linePaint.alpha = 255
        invalidate()
    }

    fun setFocusFail() {
        linePaint.alpha = 102
        invalidate()
    }

    fun getBlureArea(): Float {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            return radius
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            return defaultLineGap.toFloat() / 2
        }
        return 0f
    }

    fun getRotate(): Float {
        return lastLineDegrees % 360
    }

    fun depthTouchDownAnim(lastScale: Float) {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            val animator1 = ObjectAnimator.ofFloat(this, "scaleX", lastScale, MIN_SCALE)
            animator1.interpolator = AccelerateDecelerateInterpolator()
            animator1.duration = 300/*PGFocusView.SCALE_ANIM_TIME*/
            animator1.start()

            val animator2 = ObjectAnimator.ofFloat(this, "scaleY", lastScale, MIN_SCALE)
            animator2.interpolator = AccelerateDecelerateInterpolator()
            animator2.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    animState = ANIM_STATE_NONE
                }
            })
            animState = ANIM_STATE_DOWN
            animator2.duration = 300/*PGFocusView.SCALE_ANIM_TIME*/
            animator2.start()
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            animState = ANIM_STATE_DOWN
            lineAnimInterpolator = AccelerateInterpolator()
            lineScaleAnimStartTime = System.currentTimeMillis()
            lintScaleAnimStartScale = lastScale
            invalidate()
        }
    }

    fun depthTouchUpAnim(lastScale: Float) {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            ObjectAnimator.ofFloat(this, "scaleX", MIN_SCALE, lastScale).setDuration(300).start()
            val animatorY = ObjectAnimator.ofFloat(this, "scaleY", MIN_SCALE, lastScale).setDuration(300)
            animatorY.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    animState = ANIM_STATE_NONE
                }
            })
            animState = ANIM_STATE_UP
            animatorY.start()
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            animState = ANIM_STATE_UP
            lineAnimInterpolator = AccelerateInterpolator()
            lineScaleAnimStartTime = System.currentTimeMillis()
            lintScaleAnimStartScale = lastScale
            invalidate()
        }

    }

    fun isTouchInBlur(x: Float, y: Float): Boolean {
        if (visibility != VISIBLE) {
            return false
        }
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            return (x > lastX - measuredWidth / 2) && (x < lastX + measuredWidth / 2)
                    && (y > lastX - measuredHeight / 2) && (y < lastX + measuredHeight / 2)
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            invertMatrix.reset()
            touchPoint[0] = x
            touchPoint[1] = y
            invertMatrix.mapPoints(inverTouchPoint, touchPoint)
            return (inverTouchPoint[1] >= lastY - (lastLineScaleFactor * defaultLineGap) / 2 && inverTouchPoint[1] <= lastY + (lastLineScaleFactor * defaultLineGap) / 2)
        }
        return false
    }
}