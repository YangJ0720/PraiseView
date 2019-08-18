package com.example.praise

import android.content.Context
import android.graphics.*
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * 功能描述
 * @author YangJ
 * @since 2019/8/18
 */
class PraiseIconView : View, View.OnTouchListener {

    companion object {
        // 默认透明度
        private const val DEFAULT_ALPHA = 255
    }

    // 圆环半径
    private var mRadius = 0.0f
    private var mAlpha = DEFAULT_ALPHA
    private var mCircleColor = Color.RED
    // 是否为正常状态
    private var mIsNormal = true
    //
    private var mBitmapNormalResId = 0
    private var mBitmapPressedResId = 0
    // 未选中显示的图片
    private lateinit var mBitmapNormal: Bitmap
    // 选中显示的图片
    private var mBitmapPressed: Bitmap? = null
    // 画笔
    private lateinit var mPaint: Paint
    //
    private var mListener: OnStateChangedListener? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.PraiseIconView)
        array?.let { it ->
            mCircleColor = array.getColor(R.styleable.PraiseIconView_circleColor, Color.RED)
            mBitmapNormalResId = array.getResourceId(R.styleable.PraiseIconView_bitmapNormal, 0)
            mBitmapPressedResId = array.getResourceId(R.styleable.PraiseIconView_bitmapPressed, 0)
            it.recycle()
        }
        mBitmapNormal = BitmapFactory.decodeResource(resources, mBitmapNormalResId)
        // 初始化画笔
        mPaint = Paint()
        mPaint.color = mCircleColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10.0f
        // 设置触摸事件监听器
        setOnTouchListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mBitmapNormal.recycle()
        mBitmapPressed?.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 根据位图宽高决定控件大小
        val measuredWidth = mBitmapNormal.width
        val measuredHeight = mBitmapNormal.height
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        // 绘制圆环
        onDrawCircle(canvas)
        // 绘制位图
        onDrawBitmap(canvas)
    }

    /**
     * 绘制圆环
     */
    private fun onDrawCircle(canvas: Canvas) {
        val cx = (measuredWidth / 2).toFloat()
        val cy = (measuredHeight / 2).toFloat()
        mPaint.alpha = mAlpha
        canvas.drawCircle(cx, cy, mRadius, mPaint)
    }

    /**
     * 绘制位图
     */
    private fun onDrawBitmap(canvas: Canvas) {
        val bitmap = getTargetBitmap()
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null)
    }

    /**
     * 执行圆环缩放动画
     */
    private fun execCircleScale() {
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                mAlpha = (DEFAULT_ALPHA - DEFAULT_ALPHA * interpolatedTime).toInt()
                mRadius = measuredWidth / 2 * interpolatedTime
                invalidate()
            }
        }
        animation.interpolator = LinearOutSlowInInterpolator()
        animation.duration = 200
        startAnimation(animation)
    }

    /**
     * 根据当前选中状态获取对应的位图
     */
    private fun getTargetBitmap(): Bitmap? {
        return if (mIsNormal) {
            mBitmapNormal
        } else {
            if (mBitmapPressed == null) {
                mBitmapPressed = BitmapFactory.decodeResource(resources, mBitmapPressedResId)
            }
            mBitmapPressed
        }
    }

    /**
     * 选中状态变更监听器
     */
    interface OnStateChangedListener {
        fun onChanged(isNormal: Boolean)
    }

    /**
     * 设置选中状态变更监听器
     */
    fun setOnStateChangedListener(listener: OnStateChangedListener) {
        this.mListener = listener
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (MotionEvent.ACTION_UP == event?.action) {
            execCircleScale()
            //
            mListener?.onChanged(mIsNormal)
            // 将选中状态反转
            mIsNormal = !mIsNormal
        }
        return true
    }

}