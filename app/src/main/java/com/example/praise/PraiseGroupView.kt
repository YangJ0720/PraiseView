package com.example.praise

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * 功能描述
 * @author YangJ
 * @since 2019/8/18
 */
class PraiseGroupView : LinearLayout {

    private lateinit var mPraiseTextView: PraiseTextView

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context?) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.view_praise, this)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //
        val praiseIconView = view.findViewById<PraiseIconView>(R.id.praiseIconView)
        praiseIconView.setOnStateChangedListener(object : PraiseIconView.OnStateChangedListener {
            override fun onChanged(isNormal: Boolean) {
                if (isNormal) {
                    add()
                } else {
                    del()
                }
            }

        })
        //
        mPraiseTextView = view.findViewById(R.id.praiseTextView)
        // 设置背景颜色
        setBackgroundColor(Color.LTGRAY)
    }

    fun setNumber(number: Int) {
        mPraiseTextView.setNumber(number)
    }

    fun add() {
        mPraiseTextView.add()
    }

    fun del() {
        mPraiseTextView.del()
    }
}