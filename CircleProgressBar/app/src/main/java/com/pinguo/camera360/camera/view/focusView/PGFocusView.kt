package com.pinguo.camera360.camera.view.focusView

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import com.pinguo.camera360.lib.camera.mvp.IFocusView
import com.pinguo.camera360.lib.camera.view.PreviewConstants
import kotlinx.android.synthetic.main.layout_pgfocus.view.*
import us.pinguo.foundation.mvp.ViewController.IZoomCallback

class PGFocusView : RelativeLayout {

    companion object {
        val SCALE_ANIM_TIME = 300L
    }

    private val TOUCH_STATE_DEFAULT = -1
    private val TOUCH_STATE_DOWN = 0
    private val TOUCH_STATE_UP = 1
    private val TOUCH_STATE_LONG_PRESS = 2
    private val TOUCH_STATE_MOVE = 3
    private val TOUCH_STATE_FLING = 5

    private val MSG_HIDE_ALL_UI_TIME = 3000L
    private val MSG_HIDE_ALL_UI_POSITION = 1
    private val MSG_HIDE_ALL_UI = 2
    private val MSG_RESET_UI_POSITION = 1
    private val MIN_SCALE = 0.7f

    private var viewCircleWidth = 0
    private var viewCircleHeight = 0
    private var viewExposureWidth = 0
    private var viewExposureHeight = 0
    private var viewDistanceWidth = 0
    private var viewDistanceHeight = 0
    private var viewLockWidth = 0
    private var viewLockHeight = 0
    private var viewDepthWidth = 0
    private var viewDepthHeight = 0
    //上一次FocusView的位置
    private var lastX = 0f
    private var lastY = 0f
    //预览区经对角线的长度
    private var preR = 0f
    private var touchState = TOUCH_STATE_DEFAULT
    private var isDown = false//false
    private var isSupportExposure = true//false
    private var isShowBlur = true //false
    private var isShowFocusDistance = false

    private var currentX = 0f
    private var currentY = 0f

    private var isTouchInFocusView = false

    private var focusViewCallBack: IFocusView? = null
    private var lastRotateDegrees = 0f
    private var hasInitPoint = false

    private var currentLineScale = 1.0f
    private var currentCicleScale = 1.0f
    private var lastCircleScale = 1.0f
    private var lastLineScale = 1.0f
    private var lastWidth = 0
    private var lastHeight = 0

    private var focusLockState = -1
    private var isScale = false
    private var zoomBarCallBack: IZoomCallback? = null
    private var blurState = 0

    private val handler = @SuppressLint("HandlerLeak")
    object : android.os.Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_HIDE_ALL_UI -> {
                    //隐藏所有UI
                    hideAllView()
                }
                MSG_RESET_UI_POSITION -> {
                    //重置UI位置为中心点
//                    setUIPosition(mCurrentX, mCurrentY)
                }
            }
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        initListener()
//        pg_focus_center_rect.setAlpht(125)
    }


    private fun initListener() {
        initFocusExposureListener()
        initFocusDistanceListener()
    }

    private fun initFocusExposureListener() {
        pg_focus_exposure.setOnSeekBarChangeListener(object : PGSeekBar.OnSeekBarChangeListener {

            override fun onSeekRateChanged(seekBar: PGSeekBar, rate: Float) {
                focusViewCallBack?.onExposureValueChanged(rate)
                removeHideAllViewMessage()
            }


            override fun onActionUp() {
                removeHideAllViewMessage()
                focusViewCallBack?.onExposureValueSeekDone()
                if (pg_focus_circle.visibility == VISIBLE || pg_focus_rect.visibility == VISIBLE) {
                    showDepthView()
                }
            }

        })

        pg_focus_exposure.setOnDrawListener(object : PGSeekBar.OnDrawListener {
            override fun onHorizontalDrawLineFinish(canvas: Canvas, line1Left: Int, line1Right: Int, line2Left: Int, line2Right: Int) {
            }

            override fun onDrawState(isDrawLine: Boolean) {
                if (isDrawLine) {
                    hideDepthView()
                }
            }
        })
    }

    private fun initFocusDistanceListener() {
        pg_focus_distance.setOnSeekBarChangeListener(object : PGSeekBar.OnSeekBarChangeListener {
            override fun onSeekRateChanged(seekBar: PGSeekBar, rate: Float) {
                focusViewCallBack?.onFocusDistanceChanged(rate)
            }

            override fun onActionUp() {
            }

        })
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        setViewPosition(currentX, currentY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var preWidth = measuredWidth
        var preHeight = measuredHeight
        preR = Math.sqrt((preHeight * preHeight + preWidth * preWidth).toDouble()).toFloat()

        measureFocusCircleView()
        measureDepthView()
        measureExposureView()
        measureFocusDistanceView()
        measureFocusLockView()
        //初始化点击坐标点
        if (hasInitPoint) {
            hasInitPoint = preWidth == lastWidth && preHeight == lastHeight
        }
        lastWidth = preWidth
        lastHeight = preHeight
        initTouchPoint(false)
    }

    private fun initTouchPoint(init: Boolean) {
        if (!hasInitPoint) {
            lastX = measuredWidth.toFloat() / 2
            currentX = lastX
            lastY = measuredHeight.toFloat() / 2
            currentY = lastY
            hasInitPoint = init
        }
    }

    fun initBlurState(blurState: Int) {
        isShowBlur = blurState != PreviewConstants.BLUR_OFF
        this.blurState = blurState
        pg_focus_blur.setBlurState(blurState)
        scaleDepthView(getCurrentScale())
    }

    fun changeBlurState(blurState: Int) {
        isShowBlur = (blurState != PreviewConstants.BLUR_OFF && blurState != PreviewConstants.BLUR_MAN)
        this.blurState = blurState
        pg_focus_blur.setBlurState(blurState)
        if (isShowBlur) {
            showDepthView()
            pg_focus_blur.visibility = View.VISIBLE
        } else {
            pg_focus_blur.visibility = View.INVISIBLE
        }
    }

    private fun measureDepthView() {
        viewDepthWidth = pg_focus_blur.measuredWidth
        viewDepthHeight = pg_focus_blur.measuredHeight
    }

    private fun measureFocusCircleView() {
        viewCircleWidth = pg_focus_circle.measuredWidth
        viewCircleHeight = pg_focus_circle.measuredHeight
    }

    private fun measureExposureView() {
        viewExposureWidth = pg_focus_exposure.measuredWidth
        viewExposureHeight = pg_focus_exposure.measuredHeight
    }

    private fun measureFocusDistanceView() {
        viewDistanceWidth = pg_focus_distance.measuredWidth
        viewDistanceHeight = pg_focus_distance.measuredHeight
    }

    private fun measureFocusLockView() {
        viewLockWidth = pg_focus_rect.measuredWidth
        viewLockHeight = pg_focus_rect.measuredHeight
    }

    private fun removeHideAllViewMessage() {
        handler?.removeMessages(MSG_HIDE_ALL_UI)
    }

    private fun showExposureView() {
        if (isSupportExposure && pg_focus_exposure != null && pg_focus_exposure.visibility != View.VISIBLE) {
            pg_focus_exposure.visibility = View.VISIBLE
        }
    }

    private fun showFocusLockTips() {
        pg_focus_lock_tips.visibility = View.VISIBLE
    }

    fun showFocusLockView() {
        if (pg_focus_rect != null && pg_focus_rect.visibility != View.VISIBLE) {
            pg_focus_rect.visibility = View.VISIBLE
        }
    }


    fun showFocusDistanceView() {
        if (isShowFocusDistance && pg_focus_distance != null && pg_focus_distance.visibility != View.VISIBLE) {
            pg_focus_distance.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(pg_focus_distance, "alpha", 0.2f, 1f).setDuration(SCALE_ANIM_TIME).start()
        }
    }

    fun showDepthView() {
        if (pg_focus_blur == null) {
            return
        }
        pg_focus_blur.reset()
        if (isShowBlur && pg_focus_blur.visibility != View.VISIBLE) {
            pg_focus_blur.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(pg_focus_blur, "alpha", 0f, 1f).setDuration(SCALE_ANIM_TIME).start()
        }
        if (isShowBlur) {
            scaleDepthView(getCurrentScale())
        }
        if (zoomBarCallBack != null) {
            zoomBarCallBack?.hideZoomSeekBar(0)
        }
    }

    fun showMeteringView() {
        setViewPosition(currentX, currentY)
        pg_focus_circle.setFocusState(0)
    }

    fun showDepthUI() {
        pg_focus_blur.reset()
        if (isSupportExposure) {
            isSupportExposure = false
        }
        setDepthViewPosition(currentX, currentY)
        setLastPosition(currentX, currentY)
        if (isShowBlur && pg_focus_blur.visibility == View.INVISIBLE) {
            ObjectAnimator.ofFloat(pg_focus_blur, "alpha", 0f, 1f).setDuration(SCALE_ANIM_TIME).start()
        }
        if (isShowBlur) {
            scaleDepthView(getCurrentScale())
        }

        if (zoomBarCallBack != null) {
            zoomBarCallBack?.hideZoomSeekBar(0)
        }
    }

    private fun showFocusCircleView() {
        if (isDown) {
            startTouchUpAnim()
        }
        pg_focus_circle.setIsDrawRect(true)
        pg_focus_circle.setFocusState(PGFocusShape.FOCUS_START)
        if (pg_focus_circle != null && pg_focus_circle.visibility != View.VISIBLE) {
            pg_focus_circle.visibility = View.VISIBLE
        }
    }

    private fun showFocusView() {
        showFocusCircleView()
    }

    private fun showCenterRectView() {
        pg_focus_circle.setIsDrawRect(true)
        pg_focus_circle.invalidate()
    }

    private fun showFocusRect() {
        showFocusLockView()
        hideFocusDistanceView()
        showCenterRectView()
        hideFocusCircleView()
        hideExposureView()
    }

    private fun hideCenterRectView() {
        pg_focus_circle.setIsDrawRect(false)
        pg_focus_circle.invalidate()
    }

    private fun hideFocusCircleView() {
        if (pg_focus_circle != null && pg_focus_circle.visibility == View.VISIBLE) {
            pg_focus_circle.visibility = View.INVISIBLE
        }
    }

    private fun hideExposureView() {
        if (pg_focus_exposure != null && pg_focus_exposure.visibility == View.VISIBLE) {
            pg_focus_exposure.visibility = View.INVISIBLE
        }
    }

    private fun hideFocusLockView() {
        if (pg_focus_rect != null && pg_focus_rect.visibility == View.VISIBLE) {
            pg_focus_rect.visibility = View.INVISIBLE
        }
    }

    private fun hideFocusLockTips() {
        if (pg_focus_lock_tips != null && pg_focus_lock_tips.visibility == View.VISIBLE) {
            pg_focus_lock_tips.visibility = View.INVISIBLE
        }
    }

    private fun hideFocusDistanceView() {
        if (pg_focus_distance != null && pg_focus_distance.visibility == View.VISIBLE) {
            pg_focus_distance.visibility = View.INVISIBLE
        }
    }

    private fun hideDepthView() {
        if (pg_focus_blur != null && pg_focus_blur.visibility == View.VISIBLE) {
            pg_focus_blur.visibility = View.INVISIBLE
        }
    }

    private fun hideAllView() {
        hideFocusCircleView()
        hideExposureView()
        hideDepthView()
        hideFocusLockView()
        hideFocusLockTips()
    }

    fun hideLockView(){
        hideDepthView()
        hideFocusLockView()
        hideFocusDistanceView()
        hideExposureView()
    }

    private fun hideFocusOtherView() {
        hideFocusLockView()
        hideFocusLockTips()
        hideFocusDistanceView()
        hideCenterRectView()
    }

    fun setIFocusViewCallBack(focusViewCallBack: IFocusView) {
        this.focusViewCallBack = focusViewCallBack
    }

    fun resetFocusViewPosition() {
        touchState = TOUCH_STATE_DEFAULT
        currentX = measuredWidth.toFloat() / 2
        currentY = measuredHeight.toFloat() / 2
        handler.sendEmptyMessageDelayed(MSG_HIDE_ALL_UI_POSITION, MSG_HIDE_ALL_UI_POSITION + 0L)
    }

    fun hideFocusViewDelayedTimes(delayTime: Long) {
        if (handler == null) return
        handler.removeMessages(MSG_HIDE_ALL_UI)
        if (delayTime == 0L) {
            handler.sendEmptyMessage(MSG_HIDE_ALL_UI)
        } else {
            handler.sendEmptyMessageDelayed(MSG_HIDE_ALL_UI, delayTime)
        }
    }


    fun isTouchInCircle(x: Float, y: Float): Boolean {
        return isTouchInFocus(x, y) || isTouchInBlurView(x, y)
    }

    //点击是否在FocusCircle中
    private fun isTouchInFocus(x: Float, y: Float): Boolean {
        return (pg_focus_circle.visibility == View.VISIBLE)
                && ((x > lastX - viewCircleWidth / 2 && x < lastX + viewCircleWidth / 2
                && y > lastY - viewLockHeight / 2 && y < lastY + viewLockHeight / 2))
    }

    //点击是否在BlurView中
    private fun isTouchInBlurView(x: Float, y: Float): Boolean {
        return pg_focus_blur.isTouchInBlur(x, y)
    }


    private fun refreshFocusDepthPosition() {
        val p = getBlurRadius(getCurrentScale())
        focusViewCallBack?.onFocusDepthPosition(lastX / measuredWidth, lastY / measuredHeight, p.toFloat(), pg_focus_blur.getRotate())
    }

    fun focusOnTouchMove(x: Float, y: Float) {
        if (pg_focus_circle.visibility != VISIBLE && pg_focus_blur.visibility != VISIBLE) {
            return
        }
        if (isTouchInFocusView) {
            if (touchState == TOUCH_STATE_MOVE || touchState == TOUCH_STATE_UP) {
                //移动时设置当前UI位置
                setViewPosition(x, y)
                pg_focus_circle.setTouchDownPaintSize()
                if (!isDown) {
                    isDown = true
                    startTouchDownAnim()
                }
            }
        }
    }

    fun focusOnTouchUp(x: Float, y: Float) {
        if (isScale) {
            setFocusViewCurrentPosition(x, y)
        }
        dealFocusMoveUp(x, y)
        isScale = false
    }

    fun focusOnLongPress() {
        removeHideAllViewMessage()
        touchState = TOUCH_STATE_LONG_PRESS
        showFocusRect()
        setViewPosition(currentX, currentY)
        if (isShowBlur) {
            pg_focus_blur.reset()
            refreshFocusDepthPosition()
        }
        startFocusLockAnimation()
    }

    fun focusOnDown(x: Float? = null, y: Float? = null) {
        if (x != null && y != null) {
            var focusLockViewVisible = pg_focus_rect.visibility == View.VISIBLE
            isTouchInFocusView = isTouchInCircle(x, y) && !focusLockViewVisible
        }
        touchState = TOUCH_STATE_DOWN
    }

    fun focusOnFling() {
        //聚焦放大动画
        if (isDown) {
            startTouchUpAnim()
        }
    }

    fun focusOnScroll() {
        if (isTouchInFocusView && pg_focus_circle.visibility == View.VISIBLE || pg_focus_blur.visibility == View.VISIBLE) {
            touchState = TOUCH_STATE_MOVE
            removeHideAllViewMessage()
        }
    }

    fun focusSuccess() {
        pg_focus_blur.reset()
    }

    fun focusFail() {
        pg_focus_circle.setFocusFail()
        pg_focus_blur.setFocusFail()
    }


    private fun dealFocusMoveUp(x: Float, y: Float) {
        if ((touchState == TOUCH_STATE_MOVE || touchState == TOUCH_STATE_FLING) && !isScale &&
                (pg_focus_circle.visibility == VISIBLE || pg_focus_blur.visibility == VISIBLE)) {
            if (isDown) {
                startTouchUpAnim()
            }
            focusViewCallBack?.startFocusOnPosition(x, y, false)
        }
    }

    private fun dealFocusLockBreak() {
        if (focusLockState == -1 && touchState == TOUCH_STATE_LONG_PRESS) {
            pg_focus_rect.stopAnimation()
//            hideCenterRectView()
            focusViewCallBack?.startFocusOnPosition(currentX, currentY, false)
        }
    }

    fun depthScaleBegin() {
        isScale = true
        removeHideAllViewMessage()
        hideExposureView()
    }

    fun depthOnScale(scaleFactor: Float) {
        Log.d("PGFocusView","setScale:$scaleFactor")
        var scale = scaleFactor * getLastScale()
        scale = Math.max(MIN_SCALE, Math.min(scale,2.0f))
        scaleDepthView(scale)
    }

    private fun setCurrentScale(currentScale: Float) {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            currentCicleScale = currentScale
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            currentLineScale = currentScale
        }
    }

    private fun getCurrentScale(): Float {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            return currentCicleScale
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            return currentLineScale
        }
        return 1.0f
    }

    private fun setLastScale(lastScale: Float) {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            lastCircleScale = lastScale
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            lastLineScale = lastLineScale
        }
    }

    private fun getLastScale(): Float {
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            return lastCircleScale
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            return lastLineScale
        }
        return 1.0f
    }

    fun depthScaleEnd() {
        setLastScale(getCurrentScale())
        isScale = true
        if (isDown) {
            startTouchUpAnim()
        }
        showExposureView()
    }

    fun blurRotateBegin() {
        removeHideAllViewMessage()
        hideExposureView()
    }

    fun blurRotate(rotate: Float) {
        lastRotateDegrees -= rotate
        pg_focus_blur.setRotate(lastRotateDegrees)
        focusViewCallBack?.onFocusDepthPosition(lastX / measuredWidth, lastY / measuredHeight, getBlurRadius(getCurrentScale()), pg_focus_blur.getRotate())
    }

    fun blurRotateEnd() {
        showExposureView()
    }

    private fun setCenterFocusRectPosition(x: Float, y: Float) {
    }

    private fun setLastPosition(x: Float, y: Float) {
        lastX = x
        lastY = y
    }

    private fun setFocusDistancePoition(x: Float, y: Float) {
        val left = x - viewDistanceWidth / 2
        val top = y + viewDistanceHeight / 2
        val right = left + viewDistanceWidth
        val bottom = top + viewDistanceHeight
        pg_focus_distance.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    private fun setFocusLockViewPosition(x: Float, y: Float) {
        val left = x - viewLockWidth / 2
        val top = y - viewLockHeight / 2
        val right = left + viewLockWidth
        val bottom = top + viewLockHeight
        pg_focus_rect.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    private fun setFocusCirclePosition(x: Float, y: Float) {
        val left = x - viewCircleWidth / 2
        val top = y - viewCircleHeight / 2
        val right = left + viewCircleWidth
        val bottom = top + viewCircleHeight
        pg_focus_circle.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    private fun setDepthViewPosition(x: Float, y: Float) {
        pg_focus_blur.updatePosition(x, y)
    }

    private fun setExposureViewPosition(x: Float, y: Float) {
        val circleRadius = viewCircleWidth / 2
        var left = x + circleRadius - viewExposureWidth / 3
        val top = y - circleRadius - (viewExposureHeight - viewCircleHeight) / 2
        var right = left + viewExposureWidth
        val bottom = top + viewExposureHeight
        if (x > width / 2) {
            left = x - circleRadius - viewExposureWidth + viewExposureWidth / 3
            right = left + viewExposureWidth
        }
        pg_focus_exposure.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    fun setFocusViewCurrentPosition(x: Float, y: Float) {
        hasInitPoint = true
        currentX = x
        currentY = y
    }

    private fun setViewPosition(x: Float, y: Float) {
        setFocusCirclePosition(x, y)
        setDepthViewPosition(x, y)
        setExposureViewPosition(x, y)
        setFocusLockViewPosition(x, y)
        setFocusDistancePoition(x, y)
        setLastPosition(x, y)
        invalidate()
    }

    fun getFocusCurrentPosition(): FloatArray {
        val focusPoint = floatArrayOf(currentX, currentY)
        return focusPoint
    }

    private fun scaleDepthView(scale: Float) {
        setCurrentScale(scale)
        pg_focus_blur.setScale(scale)
        val p = getBlurRadius(scale)
        focusViewCallBack?.onFocusDepthPosition(lastX / measuredWidth, lastY / measuredHeight, p, pg_focus_blur.getRotate())
    }

    private fun getBlurRadius(scale: Float): Float {
        var p = 0f
        val focusDepthRadius = pg_focus_blur.getBlureArea()
        if (blurState == PreviewConstants.BLUR_CIRCLE) {
            p = focusDepthRadius * scale / (preR / 2)
        } else if (blurState == PreviewConstants.BLUR_LINE) {
            p = focusDepthRadius * scale / preR
        }
        return p
    }

    fun focusOnSingleTapConfirmed() {
        /*if (touchState == TOUCH_STATE_UP) {
            return
        }*/

        initTouchPoint(true)
        touchState = TOUCH_STATE_UP
        setViewPosition(currentX, currentY)
        hideFocusOtherView()
        removeHideAllViewMessage()
        showFocusView()
        showExposureView()
        startFocusAnimation()
        showDepthView()
        isTouchInFocusView = false
    }

    fun setExposureSeekRate(rate: Float) {
        if (isSupportExposure) {
            pg_focus_exposure.setCurrentSeekRate(rate)
        }
    }

    fun setDistanceSeekValue(rate: Float) {
        if (isShowFocusDistance) {
            pg_focus_distance.setCurrentSeekRate(rate)
        }
    }

    fun isSupportFocusDistance(isSupport: Boolean) {
        isShowFocusDistance = isSupport
    }

    fun getFocusDepthVisibity(): Boolean {
        return pg_focus_blur.visibility == View.VISIBLE
    }

    private fun startFocusLockAnimation() {
        if (pg_focus_rect.visibility != View.VISIBLE) {
            return
        }
        pg_focus_rect.startDrawAnimation()
        pg_focus_rect.setAnimationStateListener(object : PGFocusRect.AnimationStateListener {
            override fun onFocusStart() {

            }

            override fun onScaleStart() {
                hideCenterRectView()
            }

            override fun onAnimationEnd() {
                if (pg_focus_blur.visibility != View.VISIBLE) {
                    showDepthView()
                }
                if (pg_focus_rect.visibility != View.VISIBLE) {
                    return
                }
                showFocusDistanceView()
                showExposureView()
                showFocusLockTips()
            }

            override fun onAnimationState(state: Int) {
                focusLockState = state
            }

        })
    }

    private fun startFocusAnimation() {
        pg_focus_circle.startDrawAnimation()
    }

    private fun startTouchUpAnim() {
        focusCircleTouchDownAnim()
        hideExposureView()
        pg_focus_blur.depthTouchUpAnim(getCurrentScale())
    }

    private fun startTouchDownAnim() {
        focusCircleTouchDownAnim()
        hideExposureView()
        pg_focus_blur.depthTouchDownAnim(getCurrentScale())
    }

    private fun focusCircleTouchDownAnim() {
        if (pg_focus_circle != null) {
            val animator1 = ObjectAnimator.ofFloat(pg_focus_circle, "scaleX", 1f, MIN_SCALE)
            animator1.interpolator = AccelerateDecelerateInterpolator()
            animator1.duration = SCALE_ANIM_TIME
            animator1.start()

            val animator2 = ObjectAnimator.ofFloat(pg_focus_circle, "scaleY", 1f, MIN_SCALE)
            animator2.interpolator = AccelerateDecelerateInterpolator()
            animator2.duration = PGFocusView.SCALE_ANIM_TIME
            animator2.start()
        }
    }

    private fun focusCircleTouchUpAnim() {
        if (pg_focus_circle != null) {
            val animator1 = ObjectAnimator.ofFloat(pg_focus_circle, "scaleX", MIN_SCALE, 1f)
            animator1.interpolator = AccelerateDecelerateInterpolator()
            animator1.duration = SCALE_ANIM_TIME
            animator1.start()

            val animator2 = ObjectAnimator.ofFloat(pg_focus_circle, "scaleY", MIN_SCALE, 1f)
            animator2.interpolator = AccelerateDecelerateInterpolator()
            animator2.duration = PGFocusView.SCALE_ANIM_TIME
            animator2.start()
            pg_focus_circle.setTouchUpPanitSize()
        }
    }

    fun isTouchInFocusArea(): Boolean {
        return isTouchInCircle(currentX, currentY)
    }

    fun setZoomBarCallBack(zoomBarCallBack: IZoomCallback) {
        this.zoomBarCallBack = zoomBarCallBack
    }

    fun isFocusCircleShow(): Boolean {
        return pg_focus_circle.visibility == View.VISIBLE
    }

    fun resetExposure(){
        if (isSupportExposure){
            pg_focus_exposure.reset()
        }
    }

    fun showTipsOnly(showTipsOnly:Boolean){
        if (showTipsOnly){
            showFocusLockTips()
        } else {
            showDepthView()
            showFocusLockView()
            showFocusDistanceView()
            showExposureView()
            showFocusLockTips()
        }
    }

}