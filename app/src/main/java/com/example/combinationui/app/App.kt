package com.example.combinationui.app

import android.app.Application
import android.content.Context

/**
 *@author : Administrator
 *@descreption :
 */
class App: Application() {
    companion object {
        var context :Context ?= null
        fun getAppContext(): Context? {
            return context
        }
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }


}