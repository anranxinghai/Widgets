package com.pinguo.camera360.ui.view

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.annotation.RequiresApi
import com.pinguo.camera360.R


/**
 * <pre>
 *     author : zhaoshuanghe
 *     time   : 2018/07/04
 * </pre>
 */
class CircleProgressBar : View {

    /** 进度条最大值，默认为100 */
    private var mMaxValue = 100
    /** 当前进度值 */
    private var mCurrentValue = 0
    /** 每次扫过的角度，用来设置进度条圆弧所对应的圆心角，alphaAngle=(currentValue/maxValue)*360 */
    private var mAlphaAngle = 0f
    /** 三角的颜色，默认为Color.LTGRAY */
    private var mTriangleColor = 0
    /** 底部圆弧的颜色，默认为Color.LTGRAY */
    private var mBackgroundColor = 0
    /** 进度条圆弧块的颜色 */
    private var mProgressColor = 0
    /** 圆环的宽度 */
    private var mCircleWidth = 0f
    /** 画阴影的画笔 */
    private var mShadowCirclePaint: Paint
    /** 画圆弧的画笔 */
    private var mBackgroundCirclePaint: Paint
    /** 画扇形的画笔 */
    private var mProgressCirclePaint: Paint
    /** 画箭头的画笔 */
    private var mArrowPaint: Paint

    private var mWidth = 0f
    private var mHeight = 0f
    private var cutWidth = 0f
    private var cutHeight = 0f
    private var mProgress = 0

    private var mTrianglePaint: Paint

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0)
        mTriangleColor = ta.getColor(R.styleable.CircleProgressBar_triangleColor, Color.LTGRAY)
        mBackgroundColor = ta.getColor(R.styleable.CircleProgressBar_backgroundColor, Color.LTGRAY)
        mProgressColor = ta.getColor(R.styleable.CircleProgressBar_progressColor, Color.BLUE)
        mCircleWidth = ta.getDimensionPixelSize(R.styleable.CircleProgressBar_circleWidth, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics).toInt()).toFloat()
        ta.recycle()

        cutWidth = 10f
        cutHeight = 10f

        mTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTrianglePaint.setColor(mTriangleColor)
        mTrianglePaint.setStyle(Paint.Style.FILL)

        mShadowCirclePaint = Paint()
        mShadowCirclePaint.color = mBackgroundColor
        mShadowCirclePaint.isAntiAlias = true//抗锯齿
        mShadowCirclePaint.isDither = true//防抖动
        mShadowCirclePaint.strokeWidth = mCircleWidth

        mBackgroundCirclePaint = Paint()
        mBackgroundCirclePaint.color = mBackgroundColor
        mBackgroundCirclePaint.isAntiAlias = true//抗锯齿
        mBackgroundCirclePaint.isDither = true//防抖动
        mBackgroundCirclePaint.strokeWidth = mCircleWidth

        mProgressCirclePaint = Paint()
        mProgressCirclePaint.color = mProgressColor
        mProgressCirclePaint.isAntiAlias = true//抗锯齿
        mProgressCirclePaint.isDither = true//防抖动
        mProgressCirclePaint.strokeWidth = mCircleWidth

        mArrowPaint = Paint()
        mArrowPaint.color = mProgressColor
        mArrowPaint.isAntiAlias = true//抗锯齿
        mArrowPaint.isDither = true//防抖动

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            closeHardwareAccelerated()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = width.toFloat()
        mHeight = height.toFloat()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        val path = Path()
        if (width > cutWidth && height > cutHeight) {
            path.moveTo(cutWidth, 0f)
            path.lineTo(width.toFloat(), 0f)
            path.lineTo(width.toFloat(), height.toFloat())
            path.lineTo(0f, height.toFloat())
            path.lineTo(0f, cutHeight)
            path.quadTo(0f, 0f, cutWidth, 0f)
            //裁剪 画布为圆角画布，左上角
            canvas?.clipPath(path)
        }

        //绘制背景
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo(0f, height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.lineTo(0f, 0f)
        path.close()

        canvas?.drawPath(path, mTrianglePaint)
        val paddingBottom = paddingBottom
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop

        val width = (width - paddingLeft - paddingRight).toFloat()
        val height = (height - paddingTop - paddingBottom).toFloat()
        val centerPoint = CenterPoint()
        centerPoint.x = (width / (2 + Math.sqrt(2.0)) + paddingLeft).toFloat()
        centerPoint.y = (width / (2 + Math.sqrt(2.0)) + paddingTop).toFloat()
        val radius = Math.min(centerPoint.x, centerPoint.y) * 0.65.toFloat()

        if (mProgress == 0) {
            drawArrow(canvas, centerPoint.x, (width * 0.15 + paddingTop).toFloat(), centerPoint.x, (width * 0.45 + paddingTop).toFloat())
        } else {
        drawBackgroundCircle(canvas, centerPoint, radius)
        drawProgressCircle(canvas, centerPoint, radius)
        }
    }

    private fun drawBackgroundCircle(canvas: Canvas?, center: CenterPoint, radius: Float) {
        mBackgroundCirclePaint.shader = null
        mBackgroundCirclePaint.style = Paint.Style.FILL
        mBackgroundCirclePaint.strokeCap = Paint.Cap.ROUND//圆形线冒
        canvas?.drawCircle(center.x, center.y, radius, mBackgroundCirclePaint)

    }

    private fun drawProgressCircle(canvas: Canvas?, center: CenterPoint, radius: Float) {
        mProgressCirclePaint.shader = null
        mProgressCirclePaint.style = Paint.Style.FILL
        mProgressCirclePaint.strokeCap = Paint.Cap.ROUND//圆形线冒
        val oval = RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);//圆的外接正方形
        mAlphaAngle = mCurrentValue * 360f / mMaxValue * 1.0f
        if (mCurrentValue > 0 ) {
            mBackgroundCirclePaint.setShadowLayer((mCurrentValue.toFloat() / 1000 * 500), (mCurrentValue.toFloat() / 1000 * 500), (mCurrentValue.toFloat() / 1000 * 500),
                    /*Color.valueOf(0f, 0f, 0f, (40-mCurrentValue.toFloat()*0.26).toFloat()).toArgb()*/Color.GRAY)
        } else {
            mBackgroundCirclePaint.clearShadowLayer()
            mBackgroundCirclePaint.setShadowLayer(0.5f, 0.5f, 0.5f, 0)
        }
        Log.i("CircleProgressBar","CircleProgressBar:" + mCurrentValue)
        canvas?.drawArc(oval, -90f, mAlphaAngle, true, mProgressCirclePaint)//画圆弧

    }

    private fun drawArrow(canvas: Canvas?, x1: Float, y1: Float, x2: Float, y2: Float) {
        val arrowSize = Math.sqrt((Math.abs(x1 - x2) * Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2)).toDouble()) * 0.6
        mArrowPaint.style = Paint.Style.STROKE
        mArrowPaint.strokeWidth = mCircleWidth
        mArrowPaint.color = mProgressColor
        mArrowPaint.strokeCap = Paint.Cap.ROUND//圆形线冒
        canvas?.drawLine(x1, y1, x2, y2, mArrowPaint)
        //第一条箭头的起点
        var x3: Float
        var y3: Float
        //第二条箭头的起点
        var x4: Float
        var y4: Float

        val awrad = Math.atan(1.0)
        val arrXY1 = rotateVec(x2 - x1, y2 - y1, awrad, arrowSize)
        val arrXY2 = rotateVec(x2 - x1, y2 - y1, -awrad, arrowSize)

        x3 = (x2 - arrXY1[0]).toFloat()
        y3 = (y2 - arrXY1[1]).toFloat()

        x4 = (x2 - arrXY2[0]).toFloat()
        y4 = (y2 - arrXY2[1]).toFloat()

        val arrowPath = Path()
        arrowPath.moveTo(x2, y2)
        arrowPath.lineTo(x3, y3)
        arrowPath.lineTo(x4, y4)
        arrowPath.close()
        canvas?.drawLine(x3, y3 + 4, x2, y2 + 4, mArrowPaint)
        canvas?.drawLine(x4, y4 + 4, x2, y2 + 4, mArrowPaint)

    }

    private fun rotateVec(px: Float, py: Float, ang: Double, arrowSize: Double): Array<Double> {
        var vx = px * Math.cos(ang) - py * Math.sin(ang)
        var vy = px * Math.sin(ang) + py * Math.cos(ang)
        var d = Math.sqrt(vx * vx + vy * vy)
        vx = vx / d * arrowSize
        vy = vy / d * arrowSize
        var mathstr = arrayOf(vx, vy)
        return mathstr

    }

    fun setProgress(progress: Int) {
        var percent = progress * mMaxValue / 100
        mProgress = percent
        if (percent <= 0) {
            visibility = View.VISIBLE
            percent = 0
        } else if (percent < 100) {
            visibility = View.VISIBLE
        } /*else if (percent == 100) {
            visibility = View.GONE
        } else if (percent > 100) {
            visibility = View.GONE
            percent = 100
        }*/
        mCurrentValue = percent
        invalidate()
    }

    fun setProgress(progress: Int, useAnimation: Boolean) {
        var percent = progress * mMaxValue / 100
        if (percent < 0) {
            percent = 0
        } else if (percent > 100) {
            percent = 100
        }
        if (useAnimation) {
            var animator = ValueAnimator.ofInt(0, percent)
            animator.addUpdateListener {
                ValueAnimator.AnimatorUpdateListener {
                    mCurrentValue = it.getAnimatedValue() as Int
                    invalidate()
                }
            }
            animator.setInterpolator(OvershootInterpolator())
            animator.setDuration(1000)
            animator.start()
        } else {
            setProgress(progress)
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun closeHardwareAccelerated() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun openHardwareAccelerated() {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }


    fun getDarkerColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[1] = hsv[1] + 0.1f
        hsv[2] = hsv[2] - 0.1f
        val darkerColor = Color.HSVToColor(hsv)
        return darkerColor
    }

    class CenterPoint {
        var x: Float = 0f
        var y: Float = 0f
    }
}