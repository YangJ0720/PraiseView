package com.example.praise

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @author YangJ
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        praiseGroupView.setNumber(Random().nextInt(1000))
        btnAdd.setOnClickListener {
            praiseGroupView.add()
        }
        btnDel.setOnClickListener {
            praiseGroupView.del()
        }
    }
}
