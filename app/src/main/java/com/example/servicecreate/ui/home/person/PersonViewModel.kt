package com.example.servicecreate.ui.home.person

import androidx.lifecycle.ViewModel
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.home.gateway.GatewayListener
import com.example.servicecreate.ui.toast

/**
 * @author:SunShibo
 * @date:2023-05-21 20:27
 * @feature:
 */
class PersonViewModel: ViewModel() {
    internal var personListener: PersonListener?=null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret

    internal var oldPwd = ""
    internal var newPwd = ""
    internal var verifiedCode = ""


    fun changePassword(){
        if(checkNull()){
            val result =  repository.changePassword(token, oldPwd, newPwd, verifiedCode)
            personListener?.onChangeOver(result)
        }
    }

    fun sendCommonVerified(){
        val result = repository.sendCommonVerified(token)
        personListener?.onSendVerified(result)
    }

    private fun checkNull(): Boolean {
        if(oldPwd.isBlank() || oldPwd.isEmpty()){
            "旧密码不能为空".toast()
            return false
        }
        if(newPwd.isBlank() || newPwd.isEmpty()){
            "新密码不能为空".toast()
            return false
        }
        if(verifiedCode.isBlank() || verifiedCode.isEmpty()){
            "验证码不能为空".toast()
            return false
        }
        return true
    }
}