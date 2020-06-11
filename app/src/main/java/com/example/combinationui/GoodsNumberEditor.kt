package com.example.combinationui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout

/**
 *@author : Administrator
 *@descreption : 商品的数量操作组件
 */
class GoodsNumberEditor: RelativeLayout {
    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) :this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        LayoutInflater.from(context).inflate(R.layout.combination_layout,this,true)
      val a = context.obtainStyledAttributes(attrs,R.styleable.GoodsNumberEditor)
       val baseValue =  a.getInt(R.styleable.GoodsNumberEditor_baseValue,0)
        Log.i("TAG", "基准value是:$baseValue ")
    }


}