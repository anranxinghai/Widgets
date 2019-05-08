package com.pinguo.camera360.camera.view.focusView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import com.pinguo.camera360.R
import us.pinguo.foundation.utils.Util
import java.lang.Exception

abstract class PGFocusShape : View {

    companion object {
        val FOCUS_START = 0
        val FOCUS_SUCCESS = 1
        val FOCUS_FAILE = 2
    }


    //FocusShape画笔
    protected var shapeColor = Color.TRANSPARENT
    protected var shapePaint = Paint()
    protected var shapeStrokeWidth = 5f//线条粗细单位不用dp，用px，这样线条粗细变化更加精细
    protected var centerX = 0f
    protected var centerY = 0f
    private var DEFAULT_DISTANCE = 0f
    protected var radius = 0f
    //划线的画笔
    protected var linePaint = Paint()
    protected var lineColor = Color.TRANSPARENT
    protected var lineStrokeWidth = 2f
    private var lineLength = 0f
    private var isDrawLine = true
    private var isDrawRect = true
    private var isStop = false

    protected var startTime = 0L
    //当前动画执行的阶段（时间阶段）
    protected var currentState = -1
    protected var focusState = -1


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.PGFocusShape, defStyleAttr, 0)
        shapeColor = ta.getColor(R.styleable.PGFocusShape_shapeColor, Color.WHITE)
        lineColor = ta.getColor(R.styleable.PGFocusShape_lineColor, Color.WHITE)
        init()
    }

    protected fun init() {
        shapePaint.isAntiAlias = true
        shapePaint.color = shapeColor
        shapePaint.strokeWidth = shapeStrokeWidth
        shapePaint.style = Paint.Style.STROKE

        linePaint.isAntiAlias = true
        linePaint.color = lineColor
        linePaint.strokeWidth = lineStrokeWidth
        linePaint.style = Paint.Style.STROKE
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        centerX = measuredWidth.toFloat() / 2
        centerY = measuredHeight.toFloat() / 2
        DEFAULT_DISTANCE = centerX - paddingTop - paddingBottom - paddingLeft - paddingRight - centerX / 5
        radius = DEFAULT_DISTANCE
    }

    override fun onDraw(canvas: Canvas?) {
        if (isStop) return
        caculateDeltaDate()
        drawShape(canvas)
        drawLines(canvas)
    }

    open fun caculateDeltaDate() {
        var detalTime = getCurrentTime()
        var deltaRage: Float
        when (currentState) {
            0 -> {//动画第一阶段：画笔尺寸变化范围：5-3,缩放变化范围：0.7%-1.0%,消耗时间：100ms
                deltaRage = detalTime.toFloat() / 100
                radius = ((0.7 + deltaRage * 0.3) * DEFAULT_DISTANCE).toFloat()
                if (radius > DEFAULT_DISTANCE) {
                    radius = DEFAULT_DISTANCE
                }
                shapeStrokeWidth = 5 - deltaRage * 2
                if (shapeStrokeWidth <= 3) {
                    shapeStrokeWidth = 3f
                }
            }

            1 -> {//动画第二阶段：画笔尺寸变化范围：3-4,缩放变化范围：1.0-0.95，消耗时间：150ms
                deltaRage = detalTime.toFloat() / 150
                radius = ((1 - deltaRage * 0.05) * DEFAULT_DISTANCE).toFloat()
                if (radius <= DEFAULT_DISTANCE * 0.95) {
                    radius = (DEFAULT_DISTANCE * 0.95).toFloat()
                }
                shapeStrokeWidth = 3 + deltaRage
                if (shapeStrokeWidth >= 4) {
                    shapeStrokeWidth = 4f
                }
            }

            2 -> {//动画第三阶段：画笔尺寸变化范围：4-3,缩放变化范围：0.95-1.0，消耗时间：100ms
                deltaRage = detalTime.toFloat() / 100
                radius = ((0.95 + deltaRage * 0.05) * DEFAULT_DISTANCE).toFloat()
                if (radius >= DEFAULT_DISTANCE) {
                    radius = DEFAULT_DISTANCE
                }
                shapeStrokeWidth = 4 - deltaRage
                if (shapeStrokeWidth <= 3) {
                    shapeStrokeWidth = 3f
                }

            }
        }
    }

    /**
     * 获取各个动画阶段，时间渐进变化的差值
     *
     */
    open fun getCurrentTime(): Long {
        var deltaTime = System.currentTimeMillis() - startTime
        if (deltaTime <= 100) {
            currentState = 0
        } else if (deltaTime > 100 && deltaTime <= 250) {
            currentState = 1
        } else if (deltaTime > 250 && deltaTime <= 350) {
            deltaTime -= 250
            currentState = 2
        } else if (deltaTime > 500) {
            currentState = -1
        }
        return deltaTime
    }

    /**
     * 自定义线段，可以是圆形或正方形聚焦框四个边的线段，也可以是锁定聚焦的正方形框
     *
     */
    open fun drawLines(canvas: Canvas?) {

        lineLength = radius / 10
        val sqrt2D10 = Math.sqrt(2.0).toFloat() / 10
        if (isDrawLine) {
            //画左方线条
            canvas?.drawLine(centerX - radius, centerY, centerX - radius + lineLength, centerY, linePaint)
            //画上方线条
            canvas?.drawLine(centerX, centerY - radius, centerX, centerY - radius + lineLength, linePaint)
            //画右方线条
            canvas?.drawLine(centerX + radius - lineLength, centerY, centerX + radius, centerY, linePaint)
            //画下方线条
            canvas?.drawLine(centerX, centerY + radius - lineLength, centerX, centerY + radius, linePaint)
        }
        if (isDrawRect) {
            //画小的对焦框左侧
            canvas?.drawLine(centerX - sqrt2D10 * DEFAULT_DISTANCE, centerY - lineLength, centerX - sqrt2D10 * DEFAULT_DISTANCE + 1 / 20f * DEFAULT_DISTANCE, centerY - lineLength, linePaint)
            canvas?.drawLine(centerX - sqrt2D10 * DEFAULT_DISTANCE, centerY - lineLength, centerX - sqrt2D10 * DEFAULT_DISTANCE, centerY + lineLength, linePaint)
            canvas?.drawLine(centerX - sqrt2D10 * DEFAULT_DISTANCE, centerY + lineLength, centerX - sqrt2D10 * DEFAULT_DISTANCE + 1 / 20f * DEFAULT_DISTANCE, centerY + lineLength, linePaint)

            //画小的对焦框右侧
            canvas?.drawLine(centerX + sqrt2D10 * DEFAULT_DISTANCE - 1 / 20f * DEFAULT_DISTANCE, centerY - lineLength, centerX + sqrt2D10 * DEFAULT_DISTANCE, centerY - lineLength, linePaint)
            canvas?.drawLine(centerX + sqrt2D10 * DEFAULT_DISTANCE, centerY - lineLength, centerX + sqrt2D10 * DEFAULT_DISTANCE, centerY + lineLength, linePaint)
            canvas?.drawLine(centerX + sqrt2D10 * DEFAULT_DISTANCE - 1 / 20f * DEFAULT_DISTANCE, centerY + lineLength, centerX + sqrt2D10 * DEFAULT_DISTANCE, centerY + lineLength, linePaint)
        }
    }

    /**
     * 自定义画一个形状，比如聚焦框的外框可以是正方形，也可以是圆形,可以是长按锁定聚焦框的圆弧动画
     *
     */
    abstract fun drawShape(canvas: Canvas?)


    open fun startDrawAnimation() {
        startTime = System.currentTimeMillis()
        radius = DEFAULT_DISTANCE
        invalidate()
    }

    open fun setIsDrawLine(isDrawLine: Boolean) {
        this.isDrawLine = isDrawLine
    }


    open fun setIsDrawRect(isDrawRect: Boolean) {
        this.isDrawRect = isDrawRect
    }

    open fun setIsStop(isStop: Boolean) {
        this.isStop = isStop
    }

    fun setFocusState(state: Int?) {
        focusState = state!!
        shapePaint.alpha = 255
        linePaint.alpha = 255
        if (FOCUS_START == state) {
            invalidate()
        }
        if (FOCUS_SUCCESS == state) {
            invalidate()
        }
    }

    fun setFocusFail() {
        shapePaint.alpha = 102
        linePaint.alpha = 102
    }

}