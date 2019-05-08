package com.pinguo.camera360.camera.view.focusView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class VerticalSeekBar(context: Context, attrs: AttributeSet?) : PGSeekBar(context, attrs) {

    override fun getSeekBarGestureListener():SeekBarGestureListener{
        return VerticalSeekBarGestureListener()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthSize: Int
        var heightSize: Int
        if (heightMode == MeasureSpec.AT_MOST) {
            if (markerDrawable != null) {
                heightSize = 7 * markerDrawable!!.intrinsicHeight
                widthSize = paddingLeft + paddingRight + markerDrawable!!.intrinsicHeight
            } else {
                heightSize = (7 * markerRadius).toInt()
                widthSize = paddingLeft + paddingRight + (2 * markerRadius).toInt()
            }
            setMeasuredDimension(widthSize,heightSize)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    private fun showSeeBarLine(){
        isDrawLine = true
        drawListener?.onDrawState(isDrawLine)
        invalidate()
    }

    private fun hideSeeBarLine(){
        isDrawLine = false
        drawListener?.onDrawState(isDrawLine)
        invalidate()
    }

    protected inner class VerticalSeekBarGestureListener :SeekBarGestureListener {
        constructor() : super()

        override fun onDown(e: MotionEvent?): Boolean {
            showSeeBarLine()
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            showSeeBarLine()
            super.onScroll(e1, e2, distanceX, distanceY)
            invalidate()
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            hideSeeBarLine()
            super.onSingleTapUp(e)
            invalidate()
            return true
        }
    }



}