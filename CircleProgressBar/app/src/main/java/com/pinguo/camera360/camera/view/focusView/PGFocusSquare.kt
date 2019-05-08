package com.pinguo.camera360.camera.view.focusView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class PGFocusSquare : PGFocusShape {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    override fun drawShape(canvas: Canvas?) {
        drawSquare(canvas)
    }

    private fun drawSquare(canvas: Canvas?) {
        if (currentState >= 0) {
            if (shapeStrokeWidth <= 1) {
                shapeStrokeWidth = 1f
            } else if (shapeStrokeWidth >= 5) {
                shapeStrokeWidth = 5f
            }
            shapePaint.strokeWidth = shapeStrokeWidth
            invalidate()
        }
        canvas?.drawRect(centerX - radius, centerY - radius, centerX + radius, centerY + radius, linePaint)
    }
}