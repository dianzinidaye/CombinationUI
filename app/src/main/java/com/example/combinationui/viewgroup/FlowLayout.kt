package com.example.combinationui.viewgroup

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginStart
import com.example.combinationui.R
import com.example.combinationui.uitils.SizeUtils

/**
 *@author : Administrator
 *@descreption : viewgroup自定义控件
 */
class FlowLayout : ViewGroup {
    private var childHeight: Int = 0
    private var mData = ArrayList<String>()
    private var a: TypedArray?
    private var mLines = ArrayList<ArrayList<View>>()
    private var line = ArrayList<View>()
    private var tatolWidth = 0
    private var verticalPadding = 0
    private var horizontalPadding = 0
    private  var verticalMargin: Int = 0
    private var horizontalMargin:Int = 0
    private var itemOnclickListen:ItemOnclickListen ?= null


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr
                                                                                   ) {
        a = context?.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        if (a==null)return
        verticalPadding  = SizeUtils.dip2px( a?.getDimension(R.styleable.FlowLayout_verticalPadding, SizeUtils.dip2px(4.0f).toFloat())!!)
        horizontalPadding  = SizeUtils.dip2px( a?.getDimension(R.styleable.FlowLayout_horizontalPadding, SizeUtils.dip2px(5.0f).toFloat())!!)

        verticalMargin  = SizeUtils.dip2px(  a?.getDimension(R.styleable.FlowLayout_verticalMargin, SizeUtils.dip2px(3.0f).toFloat())!!)
        horizontalMargin  = SizeUtils.dip2px( a?.getDimension(R.styleable.FlowLayout_horizontalMargin,  SizeUtils.dip2px(4.0f).toFloat())!!)

        a?.recycle()
        Log.i("TAG", "11111111111111111111 ");
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        Log.i("TAG", "22222222222222222222222 ");
        val childCount = childCount
        if (childCount == 0) return
        Log.i("TAG", "孩子的数量$childCount ")

        mLines.clear()
        //因为会调用两次onMeasure方法,所以必须要line.clear(),也可以在这里才创建一个局部line对象
        line.clear()
        mLines.add(line)

        //这个值的大小与父控件一样,只是模式限定了AT_MOST
        var childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST)
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST)

        tatolWidth = horizontalMargin
        //测量孩子
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            //设置该child的值
            // childHeight = child.measuredHeight
            //measureChild后child.measuredWidth才有值,否则为0
            if (child.measuredWidth>=this.measuredWidth-2*horizontalMargin){
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth-3*horizontalMargin, MeasureSpec.AT_MOST)
            }
            //当child的mode为AT_MOST 测量孩子的方法可以为measureChild,或者child.measure,当mode为EXACTLY,测量child必须为child.measure
            measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec)
            //child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            if (index == 0) {
                line.add(child)
            } else {
                line.forEach {
                    tatolWidth += it.measuredWidth+horizontalMargin
                }
                if (tatolWidth + +horizontalMargin +child.measuredWidth <= parentWidth) {
                    line.add(child)
                } else {
                    line = ArrayList()
                    mLines.add(line)
                    line.add(child)
                }
                tatolWidth = horizontalMargin
            }
        }
        //测量自己
        val child = getChildAt(0)
        //设置该child的值
        childHeight = child.measuredHeight
        setMeasuredDimension(parentWidth, mLines.size * childHeight+mLines.size* verticalMargin)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.i("TAG", "3333333333333333333333333333333333 ");
        var currentLeft =0
        var currentTop = verticalMargin
        var currentBottom :Int = 0
        var currentRight: Int
        //循环布局mLines中的每个View
        mLines.forEach { it ->
            currentBottom += verticalMargin +childHeight
            currentLeft = horizontalMargin
            currentRight = 0
            it.forEach {
                currentRight += it.measuredWidth+ horizontalMargin
                Log.i("TAG", "measuredWidth==>>$measuredWidth   currentRight=>>>>$currentRight");
                if (currentRight> this.measuredWidth-horizontalMargin){
                    currentRight = this.measuredWidth-horizontalMargin
                }
                it.layout(currentLeft, currentTop, currentRight, currentBottom)
                Log.i("TAG", "currentLeft==>>$currentLeft     currentRight=>>>>$currentRight   it.measuredWidth==>>>${it.measuredWidth}      it.width==>>>${it.width}   this.measuredWidth=>>>${this.measuredWidth}");
                currentLeft = currentRight+ horizontalMargin
            }
            //上次的底部就是这次的顶部(以左上角为原点)
            currentTop +=  verticalMargin+childHeight
        }
    }



    fun setTextData(data: ArrayList<String>) {
        Log.i("TAG", "4444444444444444444444444444444 ");
        this.mData.clear()
        this.mData.addAll(data)
        //根据数据创建子View 并且添加进来
        setUpChildren()
    }

    fun setItemOnclickListener(itemOnclickListen:ItemOnclickListen){
        this.itemOnclickListen = itemOnclickListen
    }

    private fun setUpChildren() {
        //先清空原来内容
        removeAllViews()
        //添加内容
        mData.forEach{  it ->
            val text = LayoutInflater.from(context)
                .inflate(R.layout.item_text_layout, this, false) as TextView
            text.text = it
           // text.setPadding(horizontalPadding,verticalPadding,horizontalPadding,verticalPadding)
            text.setOnClickListener {
                itemOnclickListen?.onclick(it)
            }
            //设置TextView的各种参数
            //添加View
            addView(text)
        }
    }

    interface ItemOnclickListen{
        abstract fun onclick(view:View)
    }
}