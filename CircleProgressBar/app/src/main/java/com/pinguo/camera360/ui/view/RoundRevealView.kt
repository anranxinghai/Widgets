package com.pinguo.camera360.ui.view
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.widget.ScrollerCompat

/**
 * <pre>
 *     author : zhaoshuanghe
 *     time   : 2018/07/10
 * </pre>
 */
class RoundRevealView : View {

    private var mRadius: Int = 0
    private var mBitmap: Bitmap? = null
    private var mMatrix = Matrix()
    private var mWidth = 0
    private var mHeight = 0
    private var mBitmapWidth = 0
    private var mBitmapHeight = 0
    private var mScroller: ScrollerCompat
    private var mAccelerateInterpolator: AccelerateInterpolator
    private var mPaint: Paint = Paint()
    private var mDiagonal = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mAccelerateInterpolator = AccelerateInterpolator()
        mScroller = ScrollerCompat.create(context, mAccelerateInterpolator)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = width
        mHeight = height
        mDiagonal = Math.sqrt((height * height + width * width).toDouble()).toInt()

        var widthScale = mWidth.toFloat() / mBitmapWidth.toFloat()
        var heightScale = mHeight.toFloat() / mBitmapHeight.toFloat()
        if (widthScale < heightScale) {
            mMatrix.reset()
            mMatrix.postTranslate(0f, (mHeight - mBitmapHeight).toFloat() / 2)
            mMatrix.postScale(widthScale, widthScale, 0f, mBitmapHeight.toFloat() / 2 + (mHeight - mBitmapHeight).toFloat() / 2)
        } else {
            mMatrix.reset()
            mMatrix.postTranslate((mWidth - mBitmapWidth).toFloat() / 2, 0f)
            mMatrix.postScale(widthScale, widthScale, mBitmapWidth.toFloat() / 2 + (mWidth - mBitmapWidth).toFloat() / 2, 0f)
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        mBitmapWidth = bitmap.width
        mBitmapHeight = bitmap.height
    }

    override fun onDraw(canvas: Canvas?) {

        if (mScroller!!.computeScrollOffset()) {
            mRadius = mScroller.currY
            if (mRadius < mDiagonal / 2) {
//                mPaint.alpha =
                invalidate()
            } else {
                visibility = View.GONE
            }
        } else {
            mRadius = mDiagonal / 2
        }

        var mPath = Path()
        mPath.addCircle(mWidth.toFloat() / 2, mHeight.toFloat() / 2, mRadius.toFloat(), Path.Direction.CW)
        mPath.addCircle(mWidth.toFloat() / 2, mHeight.toFloat() / 2, mRadius.toFloat()/2, Path.Direction.CW)

        canvas?.clipPath(mPath, Region.Op.DIFFERENCE)
        canvas?.drawBitmap(mBitmap!!, mMatrix, mPaint)
    }

    fun startReveal() {
        visibility = View.VISIBLE
        mScroller?.startScroll(0, 0, 0, mDiagonal / 2, 10000)
        invalidate()
    }
}