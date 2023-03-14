package com.example.servicecreate.ui.auth

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.LoginResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import retrofit2.Call

/**
 * @author:SunShibo
 * @date:2023-03-09 0:03
 * @feature:
 */
interface AuthListener  {
    fun onLoginStarted()
    fun onLoginSuccess(loginResult: LiveData<Result<LoginResponse>>)
    fun onLoginFailure(msg: String){}
    fun onLoginFailure(msg: Int){}

    fun onRegisterStarted()
    fun onRegisterSuccess(registerResult: LiveData<Result<SendVerifiedResponse>>)
    fun onRegisterFailure(msg: String){}
    fun onRegisterFailure(msg: Int){}

    fun onVerifySuccess(sendResult: LiveData<Result<SendVerifiedResponse>>)
    fun onVerifyFailure(msg: String){}
    fun onVerifyFailure(msg: Int){}

    fun onCheckCodeSuccess(result: LiveData<Result<SendVerifiedResponse>>)
    fun onCheckCodeFailure(){}

}
