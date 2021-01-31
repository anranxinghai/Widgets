package com.pinguo.camera360

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.pinguo.camera360.camera.view.focusView.PGFocusView
import com.pinguo.camera360.camera.view.focusView.gesture.RotateGestureDetector
import kotlinx.android.synthetic.main.activity_camerax.*
import us.pinguo.foundation.utils.Util

class CameraXActivity : AppCompatActivity(), GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener, RotateGestureDetector.OnRotateGestureListener {


    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var rotateGestureDetector: RotateGestureDetector? = null
    private var gestureDetector: GestureDetector? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerax)
        scaleGestureDetector = ScaleGestureDetector(this, this)
        rotateGestureDetector = RotateGestureDetector(this, this)
        gestureDetector = GestureDetector(this, this)

        (pg_focus_view as PGFocusView).setFocusViewCurrentPosition(Util.getScreenWidth() / 2f, Util.getScreenHeight() / 2f)
        (pg_focus_view as PGFocusView).focusOnSingleTapConfirmed()
//        (pg_focus_view as PGFocusView).hideFocusViewDelayedTimes(300)
        capture_view.setOnTouchListener { v, event ->
            (pg_focus_view as PGFocusView).setFocusViewCurrentPosition(event?.x!!.toFloat(), event?.y.toFloat())
            try {
                rotateGestureDetector!!.onTouchEvent(event)
                scaleGestureDetector!!.onTouchEvent(event)
                gestureDetector!!.onTouchEvent(event)
            } catch (e: Exception) {
            }

            if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_UP) {
                //Action up 一般都是在GestureDetecor之后调用
                (pg_focus_view as PGFocusView).focusOnTouchUp(event?.x!!.toFloat(), event?.y.toFloat())
            }
            true
        }
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        (pg_focus_view as PGFocusView).depthScaleBegin()
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        (pg_focus_view as PGFocusView).depthScaleEnd()
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        (pg_focus_view as PGFocusView).depthOnScale(detector?.scaleFactor!!)
        return true
    }

    override fun onRotate(detector: RotateGestureDetector?): Boolean {
        (pg_focus_view as PGFocusView).blurRotate(detector!!.rotationDegreesDelta)
        return true
    }

    override fun onRotateBegin(detector: RotateGestureDetector?): Boolean {
        (pg_focus_view as PGFocusView).blurRotateBegin()
        return true
    }

    override fun onRotateEnd(detector: RotateGestureDetector?) {
        (pg_focus_view as PGFocusView).blurRotateEnd()
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        (pg_focus_view as PGFocusView).setFocusViewCurrentPosition(event?.x!!.toFloat(), event?.y.toFloat())
        (pg_focus_view as PGFocusView).focusOnSingleTapConfirmed()
//        (pg_focus_view as PGFocusView).hideFocusViewDelayedTimes(3000)
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        (pg_focus_view as PGFocusView).setFocusViewCurrentPosition(event?.x!!.toFloat(), event?.y.toFloat())
        (pg_focus_view as PGFocusView).focusOnLongPress()
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?,
                          distanceX: Float, distanceY: Float): Boolean {
        (pg_focus_view as PGFocusView).focusOnScroll()
        (pg_focus_view as PGFocusView).focusOnTouchMove(e2!!.x, e2!!.y)
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onDown(e: MotionEvent?): Boolean {
        (pg_focus_view as PGFocusView).focusOnDown(e?.x, e?.y)
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }
}