package us.pinguo.foundation.mvp.ViewController;


/**
 * Created by yuyidong on 15-5-11.
 */
public interface IZoomCallback  {
    /**
     * 初始化Zoom的SeekBar
     *
     * @param isSupport    是否支持
     * @param maxValue     最大zoom值，设置给seekbar的max
     * @param currentValue 当前zoom值，设置给seekbar
     */
    void initZoomSeekBar(boolean isSupport, int maxValue, int currentValue);

    /**
     * zoom的seekBar的回调
     *
     * @param index 范围为0~maxZoom
     */
    void onZoomSeekBarValueChanged(int index);

    /**
     * 单个手指收起事件
     *
     * @return
     */
    boolean showZoomSeekBar(boolean isAlwaysShow);

    /**
     * 触屏Scale结束的事件
     *
     * @return
     */
    boolean hideZoomSeekBar(long delay);

    /**
     * 触屏scale变化中的事件
     *
     * @param value
     * @return
     */
    boolean onZoomGestureChanged(int value);

    /**
     * 触屏scale开始的事件
     *
     * @return
     */
    boolean onZoomGestureBegin();

    void setZoomValue(int zoom);

    void onVolumeKeyZoom();

    boolean isZoomBarVisible();
}
