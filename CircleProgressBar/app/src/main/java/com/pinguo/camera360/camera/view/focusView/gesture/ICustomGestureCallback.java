package com.pinguo.camera360.camera.view.focusView.gesture;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by wangqinlong on 15-6-8.
 */
public interface ICustomGestureCallback {
    boolean onDown(MotionEvent e);

    boolean onSingleTapUp(MotionEvent e);

    boolean onSingleTapConfirmed(MotionEvent e);

    boolean onScale(ScaleGestureDetector detector);

    boolean onScaleEnd(ScaleGestureDetector detector);

    boolean onScaleBegin(ScaleGestureDetector detector);

    boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

    boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);

    boolean onInterceptMotionEvent(GestureEventName eventName, boolean isHandled);

    boolean onLongPress(MotionEvent e);

    boolean onUp(MotionEvent event);

    boolean onRotate(RotateGestureDetector detector);

    boolean onRotateBegin(RotateGestureDetector detector);

    boolean onRotateEnd(RotateGestureDetector detector);

    boolean onDoubleTap(MotionEvent e);

    // TODO 改成int，enum更慢
    enum GestureEventName {
        DOWN_EVENT,
        SINGLE_TAP_UP_EVENT,
        SINGLE_TAP_UP_CONFIRM_EVENT,
        SCALE_BEGIN_EVENT,
        SCALE_EVENT,
        SCALE_END_EVENT,
        ROTATE_BEGIN_EVENT,
        ROTATE_EVENT,
        ROTATE_END_EVENT,
        SCROLLER_EVENT,
        FLING_EVENT,
        LONG_PRESS_EVENT,
        UP_EVENT
    }
}
