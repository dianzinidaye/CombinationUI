package com.example.combinationui.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import android.widget.TextView
import com.example.combinationui.R

const val FUNCTION_DEFAULT_VALUE = 1

/**
 *@author : Administrator
 *@descreption : 仿微信聊天列表左划
 */
class SlideItemView : ViewGroup,  View.OnTouchListener {
    private var mPressX1: Float = 0.0f
    private var currentBottom: Int = 0
    private var currentRight: Int = 0
    private var currentTop = 0
    private var currentLeft: Int = 0
    private var mMoveX: Float = 0.0f
    private var mPressX: Float = 0.0f
    private var mMoveDistance: Float = 0.0f
    private var mAddView: View? = null
    private var mContentView: View? = null
    private var function: Int = FUNCTION_DEFAULT_VALUE
    private var mFunctionItemOnclickListener: FunctionItemOnclickListener? = null
    private var isOpen = false
    private val press = 0

    private val mScroller: Scroller

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mScroller = Scroller(context)
        if (context == null) return
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SlideItemView)
        function = a.getInt(R.styleable.SlideItemView_function, FUNCTION_DEFAULT_VALUE)
        a.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mAddView = LayoutInflater.from(context)
            .inflate(R.layout.item_slide_funtion_view, this, false)
        this.addView(mAddView)
        mAddView?.findViewById<TextView>(R.id.delete)?.setOnTouchListener(this)
        mAddView?.findViewById<TextView>(R.id.add)?.setOnTouchListener(this)
        mAddView?.findViewById<TextView>(R.id.top)?.setOnTouchListener(this)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            Log.i("TAG", "父控件中的DOWN ");
            mPressX = ev.x
            //在ViewGroup中,当事件为down时,不消费事件,继续向下分发
            return false
        }
        if (ev?.action == MotionEvent.ACTION_MOVE) {
            Log.i("TAG", "父控件中的MOVE ");
            //当事件为move时,则消费事件,因为子View都不需要move事件,只需要点击的事件
            super.onInterceptTouchEvent(ev)
            return true
        }

      return  true
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        if (event == null) return true
        when (event.action) {
            ACTION_DOWN -> {
                Log.i("TAG", "父控件中onTouchEvent的DOWN: ");
                mPressX = event.x
                mPressX1 = event.x

                return true
            }
            ACTION_UP -> {
                Log.i("TAG", "父控件中onTouchEvent的UP: ");
                // parent.requestDisallowInterceptTouchEvent(true)  //将父控件中的事件分发设为true,
                // 当父控件的onInterceptTouchEvent设置为true是,它不会向它的子View分发事件
               // performClick()
                Log.i("TAG", "mPressX1==>$mPressX1      event.x==>>${event.x} ");
                if (isOpen&&(mPressX1-event.x==0.0f)) {  //&&(mPressX1-event.x==0.0f)
                    //当前是open,那么点击时会关闭
                    Log.i("TAG", "点击了关闭 ");
                    mScroller.startScroll(currentLeft, 0, -currentLeft, 0, 500)
                    invalidate()
                    isOpen = !isOpen
                    return true
                }
                if (currentLeft > (-mAddView!!.measuredWidth) / 4 && currentLeft < 0) {
                    mScroller.startScroll(currentLeft, 0, -currentLeft, 0, 500)
                    isOpen = false
                }
                if ((currentLeft <= (-mAddView!!.measuredWidth) / 4) && currentLeft < 0) {
                    mScroller.startScroll(currentLeft, 0, -mAddView!!.measuredWidth - currentLeft, 0, 500)
                    isOpen = true
                }
                // requestLayout()  //该方法会重写执行onMeasure,onLayout,onDraw
                invalidate() // 该方法值会重新执行onDraw方法

            }
            ACTION_MOVE -> {
                Log.i("TAG", "父控件中onTouchEvent的MOVE: ");
                mMoveX = event.x
                mMoveDistance = mMoveX - mPressX
                Log.i("TAG", "mMoveDistance:===>$mMoveDistance       $mMoveX          $mPressX       $isOpen");
                if (isOpen&&mMoveDistance<0){
                    mPressX = mMoveX
                    return true
                }
                Log.i("TAG", "后面还能继续 ");
                currentLeft += mMoveDistance.toInt()
                when {
                    currentLeft > 0 -> {
                        Log.i("TAG", "currentLeft > 0  ");
                        currentLeft = 0
                        mMoveDistance = 0.0f
                    }
                    currentLeft < -mAddView!!.measuredWidth -> {
                        Log.i("TAG", "currentLeft < -mAddView!!.measuredWidth  ");
                        currentLeft = -(mAddView!!.measuredWidth)
                        mMoveDistance = -(mAddView!!.measuredWidth).toFloat()
                    }
                    else -> {
                       // Log.i("TAG", "-mMoveDistance===>>>${-mMoveDistance}   currentLeft=====>$currentLeft");
                        scrollBy(-mMoveDistance.toInt(), 0)
                    }
                }

                mPressX = mMoveX
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScroller.currX >= 0) {
                currentLeft = 0
            } else {
                this.scrollTo(-mScroller.currX, 0)
                currentLeft = mScroller.currX
            }

            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val contentHeightSpec: Int

        //测量SlideItemView里面包含的这一个孩子
        mContentView = getChildAt(0)
        val contentParams = mContentView?.layoutParams
        contentHeightSpec = when (contentParams?.height) {
            LayoutParams.MATCH_PARENT -> {
                MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY)
            }
            LayoutParams.WRAP_CONTENT -> {
                MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST)
            }
            else -> {
                MeasureSpec.makeMeasureSpec(contentParams!!.height, MeasureSpec.EXACTLY)
            }
        }
        mContentView?.measure(widthMeasureSpec, contentHeightSpec)

        //测量添加的滑动的选项View,高和上面的子控件一样,宽是它的3/4
        mAddView?.measure(MeasureSpec.makeMeasureSpec(parentWidth / 4 * 3, MeasureSpec.EXACTLY), contentHeightSpec)

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        //currentTop = 0
        currentRight = currentLeft + this.measuredWidth
        currentBottom = mContentView!!.measuredHeight

        mContentView?.layout(currentLeft, currentTop, currentRight, currentBottom)
        mAddView?.layout(currentRight, 0, currentRight + this.measuredWidth / 4 * 3, currentBottom)

    }

    fun setFunctionItemOnclickListener(listener: FunctionItemOnclickListener) {
        this.mFunctionItemOnclickListener = listener
    }



    interface FunctionItemOnclickListener {
        fun add()
        fun delete()
        fun top()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        //该方法时子View中的onTouch方法,当事件为move时,不消费,把事件还给父ViewGroup去处理
        //如果down的x坐标和up的坐标都一样时,让子view消费事件
        var downX = 0.0f
        var upX = 0.0f
        when (event?.action) {
            MotionEvent.ACTION_DOWN->{
                Log.i("TAG", "ACTION_DOWN ");
                parent.requestDisallowInterceptTouchEvent(true)
               downX = x
            }
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            MotionEvent.ACTION_UP->{
                Log.i("TAG", "ACTION_UP ");
                upX = x
                if (downX-upX==0.0f){
                    when (v?.id) {
                        R.id.delete -> {
                            Log.i("TAG", "delete ");
                            mFunctionItemOnclickListener?.delete()
                        }
                        R.id.add -> {
                            mFunctionItemOnclickListener?.add()
                        }
                        R.id.top -> {
                            mFunctionItemOnclickListener?.top()
                        }
                    }
                }
            }


        }
        //返回true的话onClick事件就不能触发
        return true
    }
}