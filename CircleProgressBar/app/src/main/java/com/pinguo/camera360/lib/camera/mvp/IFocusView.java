package com.pinguo.camera360.lib.camera.mvp;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v4.app.Fragment;

import us.pinguo.camerasdk.core.util.PGSize;

/**
 * Created by wangqinlong on 15-5-27.
 */
public interface IFocusView {
    void showStartFocusView();

    void showFocusSuccessView();

    //
    void showFocusFailView();

    void showLockFocusView();

    void showMeteringView();

    void showDepthView();

    void resetFocusView(long delay);

    void setTouchDownPosition(float x, float y);

    void setTouchDown();

    void showFocusMoveUp(float x, float y);

    void showFocusMove(float x, float y);

    void onFocusFling();

    void showDepthUIScale(float scaleFactor);

    void showDepthScaleBegin();

    void showDepthScaleEnd(float scaleFactor);

    void onBlurRotateBegin();

    void onBlurRotate(float rotate);

    void onBlurRotateEnd();

    void onFocusDepthPosition(float x, float y, float radiusRatio, float degrees);

    void onFocusDistanceSeekBarState(boolean isShow);

    void setFocusUIArea(int centerX, int centerY);

    PGSize getFocusAreaRange();

    void onExposureValueChanged(float value);

    void onExposureValueSeekDone();

    void onFocusDistanceChanged(float value);

    void startFocusOnPosition(float x, float y, boolean isLockFocus);

    boolean isFocusDepthVisible();


    boolean isTouchInFocusArea(float x, float y);

    Rect getFocusTouchRegion();

    void updateExposureSeekBarState(boolean isSupportExposure);

    void showCompositionLine(boolean show);

    boolean isFocusViewVisible();

    void showRotateToast(int msgId);

    Activity getActivity();

    Fragment getFragment();

    void changeAutoEffect(String effectKey);

}
