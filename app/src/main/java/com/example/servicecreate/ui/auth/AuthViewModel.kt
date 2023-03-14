package com.example.servicecreate.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.servicecreate.R
import com.example.servicecreate.logic.Repository

/**
 * @author:SunShibo
 * @date:2023-03-09 0:04
 * @feature:
 */
class AuthViewModel : ViewModel() {

    var phoneNumber: String? = null
    var password: String? = null
    var sms: String? = null
    var email: String? = null

    var login = true

    private val repository = Repository
    internal var authListener: AuthListener? = null

    private fun sendVerifiedCode(email: String) = repository.sendVerifiedCode(email)
    private fun checkVerifiedCode(email: String, code: String) =
        repository.checkVerifiedCode(email, code)

    private fun login(email: String, password: String) = repository.login(email, password)
    private fun register(email: String, code: String, password: String, phoneNumber: String) =
        repository.register(email, phoneNumber, password, code)

    fun onSendCodeButtonClick() {
        if (email.isNullOrEmpty() || email.isNullOrBlank()) {
            authListener?.onVerifyFailure(R.string.auth_verified_email_not_null)
            return
        }
        Log.e("email", email!!)
        val result = sendVerifiedCode(email!!)
        authListener?.onVerifySuccess(result)
    }

    fun onCheckCode() {
        if (sms.isNullOrEmpty() || sms.isNullOrBlank() ||
            email.isNullOrEmpty() || email.isNullOrBlank()
        ) {
            authListener?.onCheckCodeFailure()
            return
        }

        if (sms!!.length != 4) {
            return
        }

        val result = checkVerifiedCode(email!!, sms!!)
        authListener?.onCheckCodeSuccess(result)
    }

    fun onRegisterButtonClick() {
        if(login){
            if (email.isNullOrEmpty() || email.isNullOrBlank()) {
                authListener?.onLoginFailure(R.string.auth_verified_email_not_null)
                return
            }

            if (password.isNullOrEmpty() || password.isNullOrBlank()) {
                authListener?.onLoginFailure(R.string.auth_verified_password_not_null)
                return
            }

            authListener?.onLoginStarted()

            val result = login(email!!, password!!)

            authListener?.onLoginSuccess(result)

        }else{
            Log.e("register", "gg")
            if (email.isNullOrEmpty() || email.isNullOrBlank()) {
                authListener?.onRegisterFailure(R.string.auth_verified_email_not_null)
                return
            }

            if (sms.isNullOrEmpty() || sms.isNullOrBlank()) {
                authListener?.onRegisterFailure(R.string.auth_verified_sms_not_null)
                return
            }

            if (phoneNumber.isNullOrEmpty() || phoneNumber.isNullOrBlank()) {
                authListener?.onRegisterFailure(R.string.auth_verified_phonenumber_not_null)
                return
            }

            if (password.isNullOrEmpty() || password.isNullOrBlank()) {
                authListener?.onRegisterFailure(R.string.auth_verified_password_not_null)
                return
            }
            authListener?.onRegisterStarted()

            val result = register(email!!, sms!!, password!!, phoneNumber!!)

            authListener?.onRegisterSuccess(result)
        }
    }

}