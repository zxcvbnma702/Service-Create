package com.example.servicecreate

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import cn.wandersnail.bluetooth.BTManager
import com.example.base.kxt.initSp
import com.example.servicecreate.ui.dialogInputInfo
import com.example.servicecreate.ui.dialogMessageInfo
import com.example.servicecreate.ui.dialogOkInfo
import com.example.servicecreate.ui.dialogTitleInfo
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.style.MaterialStyle


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
        lateinit var appSecret: String
        const val userID = "userId"
        const val isLogin = "isLogin"
        const val isStore = "isStore"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initDialog()
        initBlueTooth()
        sp = initSp()
    }

    private fun initBlueTooth() {

        BTManager.getInstance().initialize(this);
        BTManager.isDebugMode = true //开启日志打印

    }

    private fun initDialog() {
        DialogX.init(this)
        DialogX.globalStyle = MaterialStyle.style()
        DialogX.titleTextInfo = dialogTitleInfo(this)
        DialogX.okButtonTextInfo = dialogOkInfo(this)
        DialogX.messageTextInfo = dialogMessageInfo(this)
        DialogX.inputInfo = dialogInputInfo(this)
        DialogX.implIMPLMode = DialogX.IMPL_MODE.DIALOG_FRAGMENT
        DialogX.DEBUGMODE = true

        MessageDialog.overrideEnterAnimRes = com.kongzue.dialogx.R.anim.anim_dialogx_bottom_enter
        MessageDialog.overrideExitAnimRes = com.kongzue.dialogx.R.anim.anim_dialogx_bottom_exit

    }


}