package com.example.servicecreate.ui.home.gateway

import androidx.lifecycle.ViewModel
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.logic.db.model.MyDevice
import com.example.servicecreate.ui.toast

/**
 * @author:SunShibo
 * @date:2023-04-09 1:03
 * @feature:
 */
class GatewayViewModel: ViewModel() {

    internal var gatewayListener: GatewayListener?=null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret

    internal var WIFIName: String = ""
    internal var WIFIPassword: String = ""

    internal var deviceList: MutableList<MyDevice> = arrayListOf()

    fun hasBytes(): Boolean{
        if(WIFIName.isBlank() || WIFIName.isEmpty()){
            "WIFI账号不能为空".toast()
            return false
        }
        if(WIFIPassword.isBlank() || WIFIPassword.isEmpty()){
            "WIFI密码不能为空".toast()
            return false
        }
        return true
    }

}