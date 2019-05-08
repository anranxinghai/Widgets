package com.pinguo.camera360.camera.view.focusView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet

class PGFocusRect : PGFocusShape {

    //每次动画圆弧进度单位
    private val SWEEP_INC = 6
    private lateinit var rectF: RectF
    private var cX = 0f
    private var cY = 0f
    private var scale = 1.2f
    private var isDrawCircle = false
    private var start1Angle = 90f
    private var start2Angle = 270f
    private var sweepAngle = 0f
    private var radiusRat = 180f
    private var stateListener:AnimationStateListener? = null


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        cX = centerX * 2 - paddingRight
        cY = centerY * 2 - paddingBottom
        rectF = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), cX, cY)
    }

    override fun drawShape(canvas: Canvas?) {
        drawArc(canvas)
    }

    override fun drawLines(canvas: Canvas?) {
        drawRect(canvas)
    }

    private fun drawArc(canvas: Canvas?) {
        if (stateListener == null) return
        if (sweepAngle > 180) {
            return
        }

        canvas?.drawArc(rectF, start1Angle, sweepAngle, false, linePaint)
        canvas?.drawArc(rectF, start2Angle, sweepAngle, false, linePaint)
        sweepAngle += SWEEP_INC
        lineStrokeWidth += 0.1f
        if (lineStrokeWidth >= 4) {
            lineStrokeWidth = 4f
        }
        scale -= 0.01f
        if (scale <= 1) {
            scale = 1f
        }
        if (sweepAngle <= 180) {
            invalidate()
        } else {
            //重置 重新绘制
            sweepAngle = 181f
            stateListener?.onFocusStart()
            isDrawCircle = true
            currentState = 0
            startTime = System.currentTimeMillis()
            invalidate()
        }
    }

    private fun drawRect(canvas: Canvas?) {
        if (!isDrawCircle) {
            return
        }
        canvas?.scale(scale, scale, centerX, centerY)
        if (sweepAngle >= 180) {
            canvas?.drawRoundRect(rectF, radiusRat, radiusRat, linePaint)
        }

        if (currentState >= 0) {
            invalidate()
        }
    }

    override fun startDrawAnimation() {
        setIsStop(false)
        currentState = -1
        sweepAngle = 0f
        radiusRat = 180f
        invalidate()
    }


    override fun caculateDeltaDate() {
        if (stateListener == null) return
        if (!isDrawCircle && currentState != -1) {
            return
        }
        stateListener?.onAnimationState(currentState)
        val deltaTime = getCurrentTime()
        var deltaRage: Long
        when (currentState) {
            0 -> {//动画第一阶段：半径变化范围:0.9-1.1,画笔尺寸变化范围：4-3 耗时150ms
                deltaRage = deltaTime / 150
                scale = 0.9f + deltaRage * 0.2f
                if (scale >= 1.1) {
                    scale = 1.1f
                }
                radiusRat = 180f - deltaRage * 180
                shapeStrokeWidth = 4 - deltaRage.toFloat()
                if (shapeStrokeWidth <= 3) {
                    shapeStrokeWidth = 3f
                }
            }

            1 -> {//动画第二阶段：半径变化范围:1.1-0.9,画笔尺寸变化范围：1-4
                if (radiusRat > 0) {
                    radiusRat = 0f
                }
                stateListener?.onAnimationEnd()
            }
        }


    }

    fun stopAnimation (){
        setIsStop(true)
    }
    override fun getCurrentTime(): Long {
        var deltaTime = System.currentTimeMillis() - startTime
        if ((deltaTime <= 300)) {
            currentState = 0
        } else if(deltaTime > 300 && deltaTime <= 400){
            deltaTime -= 300
            currentState = 1
        } else if(deltaTime > 400){
            currentState = -1
        }
        return deltaTime
    }
    fun setAnimationStateListener(stateListener: AnimationStateListener){
        this.stateListener = stateListener
    }

    interface AnimationStateListener {
        fun onFocusStart()
        fun onScaleStart()
        fun onAnimationEnd()
        fun onAnimationState(state:Int)
    }


}