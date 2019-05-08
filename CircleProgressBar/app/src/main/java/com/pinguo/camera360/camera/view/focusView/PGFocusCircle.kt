package com.pinguo.camera360.camera.view.focusView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class PGFocusCircle : PGFocusShape {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    override fun drawShape(canvas: Canvas?) {
        drawCicle(canvas)
    }


    private fun drawCicle(canvas: Canvas?) {
        if (currentState >= 0) {
            if (shapeStrokeWidth <= 1) {
                shapeStrokeWidth = 1f
            } else if (shapeStrokeWidth >= 5) {
                shapeStrokeWidth = 5f
            }
            shapePaint.strokeWidth = shapeStrokeWidth
            invalidate()
        }
        canvas?.drawCircle(centerX, centerY, radius, shapePaint)
    }

    fun setTouchDownPaintSize(){
        linePaint.strokeWidth = lineStrokeWidth * 2
        invalidate()
    }

    fun setTouchUpPanitSize(){
        linePaint.strokeWidth = shapeStrokeWidth
        invalidate()
    }

}