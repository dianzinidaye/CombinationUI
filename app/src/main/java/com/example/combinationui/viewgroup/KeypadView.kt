package com.example.combinationui.viewgroup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.combinationui.R
import com.example.combinationui.uitils.SizeUtils

/**
 *@author : Administrator
 *@descreption : 自定义数字键盘
 */

/*<!--行数-->
<attr name="rowNb" format="integer"/>
<!--列数-->
<attr name="columnNb" format="integer"/>
<!--文字尺寸-->
<attr name="textSz" format="dimension"/>
<!--按下背景颜色-->
<attr name="pressedBgColor" format="color"/>
<!--普通状态背景颜色-->
<attr name="normalBgColor" format="color"/>*/
class KeypadView : ViewGroup {
    private var childHeight: Int = 0
    private var childwidth: Int = 0
    private val DEFAULT_ROW_NB = 3
    private val DEFAULT_COLUMN_NB = 3
    private val DEFAULT_ITEMMARGIN = SizeUtils.dip2px(5.0f).toFloat()

    private var normalBgColor: Int = 0
    private var pressedBgColor: Int = 0
    private var textSz: Int = 0
    private var columnNb: Int = 0
    private var rowNb: Int = 0
    private var itemMargin = 0
    private var onItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(param: OnItemClickListener) {
        this.onItemClickListener = param

    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (context == null) return
        initAttrs(context, attrs)
        addChildren()
    }

     fun addChildren() {
        removeAllViews()
        for (i in 0 until (columnNb * rowNb)) {
            val textView = TextView(context)
            textView.text = i.toString()
            textView.background = getDrawableResuorse()
            textView.gravity = Gravity.CENTER
                        textView.setOnClickListener {
                Toast.makeText(context, i.toString(), Toast.LENGTH_SHORT).show()

                this.onItemClickListener?.click()


            }
            textView.setTextColor(Color.WHITE)
            addView(textView)
        }
    }

    private fun getDrawableResuorse(): Drawable {
        val pressedDrawable = GradientDrawable()
        pressedDrawable.cornerRadius = SizeUtils.dip2px(5.0f).toFloat()
        pressedDrawable.setStroke(SizeUtils.dip2px(1.0f), resources.getColor(R.color.colorGreyPressed))
        pressedDrawable.setColor(Color.BLACK)

        val normalDrawable = GradientDrawable()
        normalDrawable.cornerRadius = SizeUtils.dip2px(5.0f).toFloat()
        normalDrawable.setStroke(SizeUtils.dip2px(1.0f), resources.getColor(R.color.colorGreyPressed))
        normalDrawable.setColor(Color.RED)


        val bg = StateListDrawable()
        var array = IntArray(1)
        array[0] = android.R.attr.state_pressed
        bg.addState(array, pressedDrawable)
        array = IntArray(0)
        bg.addState(array, normalDrawable)
        return bg
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.KeypadView)
        rowNb = a.getInteger(R.styleable.KeypadView_rowNb, DEFAULT_ROW_NB)
        columnNb = a.getInteger(R.styleable.KeypadView_columnNb, DEFAULT_COLUMN_NB)
        textSz = a.getDimensionPixelSize(R.styleable.KeypadView_textSz, 18)
        pressedBgColor = a.getColor(R.styleable.KeypadView_pressedBgColor, Color.WHITE)
        normalBgColor = a.getColor(R.styleable.KeypadView_normalBgColor, Color.GRAY)
        itemMargin = a.getDimension(R.styleable.KeypadView_itemMargin, DEFAULT_ITEMMARGIN).toInt()

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val childSize = childCount
        childwidth = (parentWidth - (columnNb + 1) * itemMargin) / columnNb
        childHeight = (parentHeight - (rowNb + 1) * itemMargin) / rowNb
        Log.i("TAG", "parentWidth==>>$parentWidth   childwidth===>$childwidth  itemMargin==>$itemMargin");
        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childwidth, MeasureSpec.EXACTLY)
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
        for (i in 0 until childSize) {
            //当child的mode为AT_MOST 测量孩子的方法可以为measureChild,或者child.measure,当mode为EXACTLY,测量child必须为child.measure
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec)
            //measureChild(getChildAt(i),childWidthMeasureSpec,childHeightMeasureSpec)
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // getChildAt(0).layout(0, 0, getChildAt(0).measuredWidth, getChildAt(0).measuredHeight)
        // Log.i("TAG", "getChildAt(0).measuredWidth ==>>>${getChildAt(0).measuredWidth}   getChildAt(0).measuredHeight===>>${getChildAt(0).measuredHeight}");
        val childSize = childCount
        Log.i("TAG", "childCount为:$childCount ");
        var currentLeft = 0
        var currentTop = 0
        var currentRight = 0
        var currentBotton = 0
        /*  val singleWidth =  this.measuredWidth / columnNb
          val singleHeight = this.measuredHeight / rowNb*/
        Log.i("TAG", "childwidth==>$childwidth ");
        for (i in 0 until childSize) {
            currentLeft = childwidth * (i % columnNb) + itemMargin * (i % columnNb + 1)
            currentRight = (childwidth) * (i % columnNb + 1) + itemMargin * (i % columnNb + 1)
            currentTop = (childHeight) * ((i / columnNb)) + itemMargin * (i / rowNb + 1)
            currentBotton = (childHeight) * ((i / columnNb) + 1) + itemMargin * (i / rowNb + 1)
            getChildAt(i).layout(currentLeft, currentTop, currentRight, currentBotton)
        }
    }

    interface OnItemClickListener {
        abstract fun click()
    }
}