package com.example.servicecreate

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.base.kxt.initSp

/**
 * @author:SunShibo
 * @date:2022-12-23 13:55
 * @feature: Global application
 */
class ServiceCreateApplication : Application(){
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var sp: SharedPreferences
//        const val appId = "00871f090e174fb7bf20fb1a6a7f71e2"
//        const val appSecret = "863547048aa7289c0425787b4e8558bb15e68"
        const val userID = "userId"
        const val isLogin = "isLogin"
        const val isStore = "isStore"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
//        AppDatabase.invoke(this)
        sp = initSp()
    }
}