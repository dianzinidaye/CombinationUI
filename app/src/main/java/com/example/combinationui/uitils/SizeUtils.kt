package com.example.combinationui.uitils

import com.example.combinationui.app.App

/**
 *@author : Administrator
 *@descreption : dp转px
 */
class SizeUtils {
    companion object{
        fun dip2px( dpValue: Float): Int {
            val scale: Float by lazy {App.getAppContext()!!.resources.displayMetrics.density}
            return (dpValue * scale + 0.5f).toInt()
        }
    }

}