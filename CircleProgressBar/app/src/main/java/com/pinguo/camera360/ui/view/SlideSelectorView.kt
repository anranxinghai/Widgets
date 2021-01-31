package com.pinguo.camera360.ui.view

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.widget.ScrollerCompat
import com.pinguo.camera360.R
import us.pinguo.foundation.utils.Util

/**
 * <pre>
 *     author : zhaoshuanghe
 *     time   : 2018/07/10
 * </pre>
 */
class SlideSelectorView : View {

    val ITEM_NONE = "C360_None"
    val ITEM_AUTO = "C360_auto"
    //特效不透明度参数名
    private var mCurrentFilterType: String = ITEM_NONE
    private var mCurrentY: Int = 0
    private var bitmap: Bitmap
    private val ANIMATION_DURATION = 300
    private var mWidth = 0f
    private var mHeight = 0f
    private var cutWidth = 0f
    private var cutHeight = 0f
    private var mScroller: ScrollerCompat
    private var mAccelerateDecelerateInterpolator: AccelerateDecelerateInterpolator

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mAccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
        mScroller = ScrollerCompat.create(context, mAccelerateDecelerateInterpolator)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_filter_none_or_auto)
        cutWidth = Util.dpToPx(4f).toFloat()
        cutHeight = Util.dpToPx(4f).toFloat()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = width.toFloat()
        mHeight = height.toFloat()
        mCurrentY = mHeight.toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        if (mWidth > cutWidth && mHeight > cutHeight) {
            val path = Path()
            path.moveTo(cutWidth, 0f)
            path.lineTo(mWidth - cutWidth, 0f)
            path.quadTo(mWidth, 0f, mWidth, cutHeight)
            path.lineTo(mWidth, mHeight - cutHeight)
            path.quadTo(mWidth, mHeight, mWidth - cutWidth, mHeight)
            path.lineTo(cutWidth, mHeight)
            path.quadTo(0f, mHeight, 0f, mHeight - cutHeight)
            path.lineTo(0f, cutHeight)
            path.quadTo(0f, 0f, cutWidth, 0f)
            //裁剪 画布为圆角画布
            canvas?.clipPath(path)
        }

        var src = Rect(0, 0, width, height)
        var dst = Rect(0, 0, width, height)


        if (mScroller!!.computeScrollOffset()) {
            mCurrentY = mScroller.currY
            invalidate()
        } else {
            mCurrentY = height
        }

        //在移动过程中，不断截取src bitmap与View的交叉的部分图像矩形区域，绘制到bitmap与View的交叉的部分View的矩形区域dst，变量为移动距离 mCurrentY（取绝对值）
        //点击原片，Bitmap向上移动
        if (mCurrentFilterType == ITEM_NONE) {
            //  0 <= Math.abs(mCurrentY) <= height / 2 部分src、dst矩形区域计算
            if (Math.abs(mCurrentY) >= 0 && Math.abs(mCurrentY) <= height / 2) {
                src = Rect(0, 0, width, height / 2 + Math.abs(mCurrentY))
                dst = Rect(0, height / 2 - Math.abs(mCurrentY), width, height)
            }
            //  height / 2 <= Math.abs(mCurrentY) <= height 部分src、dst矩形区域计算
            else {
                src = Rect(0, Math.abs(mCurrentY) - height / 2, width, height)
                dst = Rect(0, 0, width, height / 2 * 3 - Math.abs(mCurrentY))
            }

        }
        //点击智能，Bitmap向下移动
        else if (mCurrentFilterType == ITEM_AUTO) {
            //  0 <= Math.abs(mCurrentY) <= height / 2 部分src、dst矩形区域计算
            if (Math.abs(mCurrentY) >= 0 && Math.abs(mCurrentY) <= height / 2) {
                src = Rect(0, height / 2 - Math.abs(mCurrentY), width, height)
                dst = Rect(0, 0, width, height / 2 + Math.abs(mCurrentY))
            }
            //  0 <= Math.abs(mCurrentY) <= height / 2 部分src、dst矩形区域计算
            else {
                src = Rect(0, 0, width, height / 2 * 3 - Math.abs(mCurrentY))
                dst = Rect(0, Math.abs(mCurrentY) - height / 2, width, height)
            }
        }
        canvas?.drawBitmap(bitmap, src, dst, null)

    }

    /**
     * @param filterType 取值为FilterDataManager.ITEM_NONE(选中原图)、filterType == FilterDataManager.ITEM_AUTO(选中智能)、空串(不选中状态)
     */
    fun setFilterType(filterType: String, isNeedAnimation: Boolean) {
        if (!TextUtils.isEmpty(filterType) && (filterType == ITEM_NONE || filterType == ITEM_AUTO)) {
            if (visibility != VISIBLE) {
                visibility = VISIBLE
            }
            if (isNeedAnimation) {
                if (filterType == ITEM_NONE && filterType != mCurrentFilterType) {
                    startScroll()
                } else if (filterType == ITEM_AUTO && filterType != mCurrentFilterType) {
                    startScroll()
                }
            } else {
                invalidate()
            }
        } else {
            visibility = View.INVISIBLE
        }
        mCurrentFilterType = filterType
    }

    private fun startScroll() {
        if (mCurrentFilterType == ITEM_NONE) {
            mScroller?.startScroll(0, 0, 0, height, ANIMATION_DURATION)
        } else if (mCurrentFilterType == ITEM_AUTO) {
            mScroller?.startScroll(0, 0, 0, -height, ANIMATION_DURATION)
        }
        invalidate()
    }
}