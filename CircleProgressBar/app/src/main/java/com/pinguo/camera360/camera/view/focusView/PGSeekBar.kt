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

abstract class PGSeekBar : View {

    //seek 相关属性参数
    protected var seekRate: Float = 0.5f
    protected var seekLength: Float = 0f
    protected var seekLineStart: Float = 0f
    protected var seekLineEnd: Float = 0f
    protected var listener: OnSeekBarChangeListener? = null
    protected var drawListener: OnDrawListener? = null

    //Marker（刻度当前参数位置，用marker表示） 相关属性参数
    protected var markerOffset: Float = 0f
    protected var markerRadius: Float = Util.dpToPixel(6.0f)
    //    protected var markerLargeRadius:Int = (markerRadius * 1.5f).toInt()
    protected var markerStrockWidth: Float = Util.dpToPixel(1.5f)
    protected var markerDrawable: Drawable? = null
    protected var markerColor: Int = Color.TRANSPARENT
    protected var markerPaint: Paint = Paint()

    //划线
    protected var linePaint: Paint = Paint()
    protected var lineColor: Int = 0
    protected var lineWidth = Util.dpToPixel(2f)
    //重新定义SeekBar手势
    protected lateinit var gestureDetector: GestureDetector
    //滑动
    protected var scroller: Scroller? = null
    //是否为垂直方向
    protected var isVertial: Boolean = true
    //是否要画线
    protected var isDrawLine: Boolean = true


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.PGSeekBar, defStyleAttr, 0)
        try {
            markerColor = ta.getColor(R.styleable.PGSeekBar_markerBackground, Color.TRANSPARENT)
        } catch (e: Exception) {
            markerDrawable = ta.getDrawable(R.styleable.PGSeekBar_markerBackground)
        }
        lineColor = ta.getColor(R.styleable.PGSeekBar_lineColor, Color.WHITE)
        init()
    }

    open fun init() {
        scroller = Scroller(context)
        gestureDetector = GestureDetector(context, getSeekBarGestureListener())
        markerPaint.isAntiAlias = true
        if (markerColor != Color.TRANSPARENT) {
            markerPaint.color = markerColor
        } else if (markerDrawable != null) {
            markerRadius = (markerDrawable!!.intrinsicHeight / 2).toFloat()
        } else {
            throw Throwable("both markerColor and markerDrawable have no value")
        }
        markerPaint.strokeWidth = markerStrockWidth
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        linePaint.isAntiAlias = true
        linePaint.color = lineColor
        linePaint.alpha = 200

    }

    protected open fun getSeekBarGestureListener():SeekBarGestureListener{
        return SeekBarGestureListener()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isVertial) {
            var widthMode = MeasureSpec.getMode(widthMeasureSpec)
            var widthSize: Int
            if (widthMode == MeasureSpec.AT_MOST) {
                var heightSize = MeasureSpec.getSize(heightMeasureSpec)
                widthSize = if ((markerRadius + markerStrockWidth) * 2 > lineWidth) ((markerRadius + markerStrockWidth) * 2).toInt() else lineWidth.toInt()
                if (markerDrawable != null) {
                    widthSize = if (widthSize > markerDrawable!!.intrinsicWidth) widthSize else markerDrawable!!.intrinsicWidth
                }
                widthSize += paddingLeft + paddingRight
                setMeasuredDimension(widthSize, heightSize)
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        } else {
            var heightMode = MeasureSpec.getMode(widthMeasureSpec)
            var heightSize: Int
            if (heightMode == MeasureSpec.AT_MOST) {
                var widthSize = MeasureSpec.getSize(widthMeasureSpec)
                heightSize = if ((markerRadius + markerStrockWidth) * 2 > lineWidth) ((markerRadius + markerStrockWidth) * 2).toInt() else lineWidth.toInt()
                if (markerDrawable != null) {
                    heightSize = if (heightSize > markerDrawable!!.intrinsicHeight) heightSize else markerDrawable!!.intrinsicHeight
                }
                heightSize += paddingTop + paddingBottom
                setMeasuredDimension(widthSize, heightSize)
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (seekLength.toInt() == 0) {
            if (isVertial) {
                seekLength = height - paddingTop - paddingBottom - markerStrockWidth - markerRadius * 2
                seekLineStart = paddingTop + markerStrockWidth / 2 + markerRadius
                seekLineEnd = height - paddingBottom - markerStrockWidth / 2 - markerRadius
            } else {
                seekLength = width - paddingLeft - paddingRight - markerStrockWidth - markerRadius * 2
                seekLineStart = paddingLeft + markerStrockWidth / 2 + markerRadius
                seekLineEnd = width - paddingRight - markerStrockWidth / 2 - markerRadius
            }
            markerOffset = seekRate * seekLength
        }

        //画线
        if (isDrawLine) {
            if (isVertial) {
                val left = paddingLeft + markerStrockWidth / 2 + markerRadius - lineWidth / 2
                val right = paddingLeft + markerStrockWidth / 2 + markerRadius + lineWidth / 2
                val line1Bottom = (seekLineStart + markerOffset - markerStrockWidth - markerRadius * 1.5).toFloat()
                if (line1Bottom > seekLineStart) {
                    canvas?.drawRect(left, seekLineStart, right, line1Bottom, linePaint)
                }
                val line2Top = (line1Bottom + markerStrockWidth + markerRadius * 3)
                if (line2Top <= seekLineEnd) {
                    canvas?.drawRect(left, line2Top, right, seekLineEnd, linePaint)
                }
            } else {
                val top = paddingTop + markerStrockWidth / 2 + markerRadius - lineWidth / 2
                val bottom = paddingTop + markerStrockWidth / 2 + markerRadius + lineWidth / 2
                val line1Right = (seekLineStart + markerOffset - markerStrockWidth * 2 - markerRadius * 1.5).toFloat()
                if (line1Right > seekLineStart) {
                    canvas?.drawRect(seekLineStart, top, line1Right, bottom, linePaint)
                }
                val line2Left = line1Right + markerStrockWidth + markerRadius * 4
                if (line2Left <= seekLineEnd) {
                    canvas?.drawRect(line2Left, top, seekLineEnd, bottom, linePaint)
                }
            }
        }

        //画刻度(marker)
        var markerCenterX: Float
        var markerCenterY: Float
        if (isVertial) {
            markerCenterX = paddingLeft + markerStrockWidth / 2 + markerRadius
            markerCenterY = seekLineStart + markerOffset
        } else {
            markerCenterX = seekLineStart + markerOffset
            markerCenterY = paddingTop + markerStrockWidth / 2 + markerRadius
        }
        if (markerColor != Color.TRANSPARENT) {
            canvas?.drawCircle(markerCenterX, markerCenterY, markerRadius, markerPaint)
        } else if (markerDrawable != null) {
            val markerLeft = (markerCenterX - markerRadius).toInt()
            val markerTop = (markerCenterY - markerRadius).toInt()
            val markerRight = (markerCenterX + markerRadius).toInt()
            val markerBottom = (markerCenterY + markerRadius).toInt()
            markerDrawable?.setBounds(markerLeft, markerTop, markerRight, markerBottom)
            markerDrawable?.draw(canvas!!)
        }

        //点击空白（非游标）的地方，在400ms内滚动到点击位置
        if (scroller != null && scroller!!.computeScrollOffset()) {
            markerOffset = scroller!!.currY.toFloat()
            invalidate()
        }
        super.onDraw(canvas)
    }

    fun setOnSeekBarChangeListener(listener: OnSeekBarChangeListener) {
        this.listener = listener
    }

    fun setCurrentSeekRate(currentRate: Float) {
        if (seekRate > 1) {
            seekRate = 0f
        } else if (seekRate < 0) {
            seekRate = 0f
        }
    }

    fun setIsVertial(isVertial: Boolean) {
        this.isVertial = isVertial
    }

    //边界处理函数
    protected fun getMarkerOffset(position: Float): Float {
        var pos: Float = position
        if (pos <= 0) {
            pos = 0f
        }
        if (pos >= seekLength) {
            pos = seekLength
        }
        return pos
    }

    interface OnDrawListener {
        fun onHorizontalDrawLineFinish(canvas: Canvas, line1Left: Int, line1Right: Int, line2Left: Int, line2Right: Int)

        fun onDrawState(isDrawLine: Boolean)
    }

    protected open inner class SeekBarGestureListener : GestureDetector.SimpleOnGestureListener {
        constructor() : super()

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            if (isVertial) {
                markerOffset -= distanceY
            } else {
                markerOffset -= distanceX
            }
            markerOffset = getMarkerOffset(markerOffset)
            if (listener != null && seekLength != 0f) {
                seekRate = markerOffset / seekLength
                listener?.onSeekRateChanged(this@PGSeekBar, seekRate)
            }
            invalidate()
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            var distance: Float
            if (isVertial) {
                scroller?.startScroll(0, markerOffset.toInt(), 0, (e!!.y - markerOffset).toInt(), 400)
                distance = e!!.y - markerOffset
            } else {
                scroller?.startScroll(0, markerOffset.toInt(), 0, (e!!.x - markerOffset).toInt(), 400)
                distance = e!!.x - markerOffset
            }
            markerOffset += distance
            markerOffset = getMarkerOffset(markerOffset)
            if (listener != null && seekLength != 0f) {
                seekRate = markerOffset / seekLength
                listener?.onSeekRateChanged(this@PGSeekBar, seekRate)
                listener?.onActionUp()
            }
            invalidate()
            return true
        }
    }

    fun reset() {
        seekRate = 0.5f
        markerOffset = seekLength * seekRate
        invalidate()
    }

    fun setOnDrawListener(drawListener: PGSeekBar.OnDrawListener) {
        this.drawListener = drawListener
    }

    interface OnSeekBarChangeListener {
        fun onSeekRateChanged(seekBar: PGSeekBar, rate: Float)
        fun onActionUp()
    }

}