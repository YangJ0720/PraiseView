package com.example.praise

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.math.pow

/**
 * 功能描述
 * @author YangJ
 * @since 2019/8/18
 */
class PraiseTextView : View {

    companion object {
        // 默认边距大小
        private const val DEFAULT_PADDING = 20
        // 默认字体大小
        private const val DEFAULT_TEXT_SIZE = 32
        //
        private const val STATE_NORMAL = 0
        private const val STATE_ADD = 1
        private const val STATE_DEL = 2
    }

    private var mNumber = 0
    private var mTextSize = DEFAULT_TEXT_SIZE
    private var mTextColor = Color.BLACK
    // 旧的数字，也就是当前正在显示的数字
    private var mNumberOld = IntArray(3)
    // 新的数字，也就是即将要显示的数字
    private var mNumberNew = IntArray(3)
    // 画笔
    private lateinit var mPaint: Paint
    private lateinit var mRect: Rect
    // 单个数字所占用的宽度
    private var mDigitWidth = 0.0f
    //
    private var mAnimProgress = 0.0f
    private lateinit var mAnimator: ObjectAnimator

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.PraiseTextView)
        array?.let { it ->
            mTextSize = it.getDimensionPixelSize(R.styleable.PraiseTextView_textSize, DEFAULT_TEXT_SIZE)
            mTextColor = it.getColor(R.styleable.PraiseTextView_textColor, Color.BLACK)
            it.recycle()
        }
        // 初始化画笔
        mPaint = Paint()
        mPaint.textSize = mTextSize.toFloat()
        mPaint.color = mTextColor
        //
        mRect = Rect()
        //
        mDigitWidth = mPaint.measureText("0")
        //
        mAnimator = ObjectAnimator.ofFloat(this, "animProgress", 0.0f, 1.0f)
        mAnimator.duration = 200
        mAnimator.addUpdateListener {
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureNumber()
        val measuredWidth = mRect.width() + DEFAULT_PADDING * 2
        val measuredHeight = mRect.height() + DEFAULT_PADDING * 2
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        // 绘制数字
        onDrawText(canvas)
    }

    /**
     * 绘制数字
     */
    private fun onDrawText(canvas: Canvas) {
        // 初始化数字状态为正常
        var state = STATE_NORMAL
        //
        val size = mNumberNew.size - 1
        // 计算偏移量
        val offset = mAnimProgress * measuredHeight
        for (i in size downTo 0) {
            // 计算x轴和y轴坐标
            val x = DEFAULT_PADDING + (size - i) * mDigitWidth
            val y = measuredHeight / 2.0f + mRect.height() / 2.0f
            // 通过比较数字大小来判断数字状态
            if (STATE_NORMAL == state) {
                if (mNumberOld[i] < mNumberNew[i]) {
                    // 设置数字状态为自增
                    state = STATE_ADD
                } else if (mNumberOld[i] > mNumberNew[i]) {
                    // 设置数字状态为自减
                    state = STATE_DEL
                }
            }
            // 根据数字状态显示不同的动画效果
            when (state) {
                STATE_ADD -> { // 数字自增状态，动画效果为上进下出
                    // 旧的数字
                    canvas.drawText(mNumberOld[i].toString(), x, y + offset, mPaint)
                    // 新的数字
                    canvas.drawText(mNumberNew[i].toString(), x, y * mAnimProgress, mPaint)
                }
                STATE_DEL -> { // 数字自减状态，动画效果为上出下进
                    // 旧的数字
                    canvas.drawText(mNumberOld[i].toString(), x, y - offset, mPaint)
                    // 新的数字
                    canvas.drawText(mNumberNew[i].toString(), x, y / mAnimProgress, mPaint)
                }
                else -> {
                    // 不变的数字
                    canvas.drawText(mNumberNew[i].toString(), x, y, mPaint)
                }
            }
        }
    }

    /**
     * 设置一个数字
     */
    fun setNumber(number: Int) {
        this.mNumber = number
        reset()
        mAnimator.start()
    }

    /**
     * 数字自增
     */
    fun add() {
        // 对数字自增做边界校验
        if (mNumber >= 999) return
        // 执行数字自增操作
        mNumber++
        reset()
        mAnimator.start()
    }

    /**
     * 数字自减
     */
    fun del() {
        // 对数字自减做边界校验
        if (mNumber == 0) return
        // 执行数字自减操作
        mNumber--
        reset()
        mAnimator.start()
    }

    private fun reset() {
        // 拷贝一份当前正在显示的数字，存放到旧的数组当中
        mNumberOld = mNumberNew.copyOf(mNumberNew.size)
        //
        for (i in 0 until 3) {
            val num = (mNumber / 10.0.pow(i.toDouble()) % 10).toInt()
            mNumberNew[i] = num
        }
    }

    private fun getAnimProgress(): Float {
        return mAnimProgress
    }

    private fun setAnimProgress(animProgress: Float) {
        this.mAnimProgress = animProgress
    }

    /**
     * 测量要显示的数字宽高
     */
    private fun measureNumber() {
        val sb = StringBuilder()
        mNumberNew.forEach {
            sb.append(it)
        }
        mPaint.getTextBounds(sb.toString(), 0, mNumberNew.size, mRect)
    }

}