package com.pinguo.camera360.lib.camera.view;

/**
 * Created by wangqinlong on 15-7-4.
 */
public class PreviewConstants {
    public static final int SETTING_TYPE_FLASH = 0;
    public static final int SETTING_TYPE_FRAME = 1;
    public static final int SETTING_TYPE_BLUR = 2;
    public static final int SETTING_TYPE_LED = 3;
    public static final int SETTING_TYPE_TIMER = 4;
    public static final int SETTING_TYPE_DARKCORNER = 5;
    public static final int SETTING_TYPE_TOUCHCAPTURE = 6;
    /*
     * Flash,Timer,Frame,Blur,Darkcorner.
     * 这里的顺序应该与xml中的一致
     */
    public static final int FLASH_OFF = 0;
    public static final int FLASH_AUTO = 1;
    public static final int FLASH_ON = 2;
    public static final int FLASH_TORCH = 3;

    public static final int TIMER_OFF = 0;
    public static final int TIMER_3S = 1;
    public static final int TIMER_5S = 2;
    public static final int TIMER_10S = 3;

    public static final int FRAME_1TO1 = 0;
    public static final int FRAME_3TO2 = 1;
    public static final int FRAME_5TO3 = 2;
    public static final int FRAME_16TO9 = 3;
    public static final int FRAME_4TO3 = 4;
    public static final int FRAME_2TO1 = 5;
    public static final int FRAME_FULL = 6;

    public static final int FRAME_BLUR_CIRCLE = 7;
    public static final int FRAME_BLUR_RECT = 8;

/*    public static final int BLUR_OFF = 0;
    public static final int BLUR_ON = 1;
    public static final int DARK_CORNER_OFF = 0;
    public static final int DARK_CORNER_ON = 1;
    public static final int LED_OFF = 0;
    public static final int LED_ON = 1;
    public static final int TOUCH_CAPTURE_OFF = 0;
    public static final int TOUCH_CAPTURE_ON = 1;*/

    public static final int OFF = 0;
    public static final int ON = 1;

    public static final int BLUR_OFF = 0;
    //人型虚化
    public static final int BLUR_MAN = 1;
    public static final int BLUR_CIRCLE = 2;
    public static final int BLUR_LINE = 3;
}
