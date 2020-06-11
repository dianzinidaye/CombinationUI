package com.example.combinationui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.combinationui.viewgroup.FlowLayout
import com.example.combinationui.viewgroup.KeypadView
import com.example.combinationui.viewgroup.SlideItemView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.combination_layout.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //FlowLayout的例子
        /*                val mText = ArrayList<String>()
                        mText.add("喇叭")
                        mText.add("喇叭4")
                        mText.add("喇叭5")
                        mText.add("喇叭6")
                        mText.add("喇叭7")
                        mText.add("喇叭8")
                        mText.add("喇叭9")
                        mText.add("这是很长的一条aaa")
                        mText.add("这是很长的一条aaabb")
                        mText.add("这是很长的一条aaabb")
                        mText.add("这是很长的一条aaab")
                        mText.add("这是很长的一条aaabbb")
                        mText.add("这是很长的一条aaab")
                        mText.add("这是很长的一条aaabb")
                        mText.add("这是很长的一条aaab")
                        mText.add("这是很长的一条这是很长的一条这是很长的一条这是很长的一条这是很长的一条这是很长的一条")
                        flowLayout.setTextData(mText)
                        flowLayout.setItemOnclickListener(object : FlowLayout.ItemOnclickListen {
                            override fun onclick(view: View) {
                                val text = view as TextView
                                Log.i("TAG", "点击的item文字为: ${text.text} ");
                                runOnUiThread {
                                    Toast.makeText(this@MainActivity,text.text, Toast.LENGTH_SHORT).show()
                                }
                            }

                        })*/


        /*  // KeypadView 例子
          keyPadView.setOnItemClickListener(object : KeypadView.OnItemClickListener {
                    override fun click() {
                        Log.i("TAG", "进入了这里 ");
                        startActivity(Intent(this@MainActivity,SecondActivity::class.java))
                    }

                })*/

        slideItemView.setOnClickListener {
           // Log.i("TAG", "setOnClickListener  ");

        }
        slideItemView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
               // Log.i("TAG", "setOnTouchListener } ");

                return false
            }

        })

        slideItemView.setFunctionItemOnclickListener(object : SlideItemView.FunctionItemOnclickListener {
            override fun add() {
                Log.i("TAG", "add be press ")
            }

            override fun delete() {
                Log.i("TAG", "delete be press ")            }

            override fun top() {
                Log.i("TAG", "top be press ")            }

        })

    }
}
