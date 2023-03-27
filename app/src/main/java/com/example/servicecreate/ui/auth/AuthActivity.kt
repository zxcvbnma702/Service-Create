package com.example.servicecreate.ui.auth

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.activity.BaseActivity
import com.example.base.kxt.toast
import com.example.base.ui.util.StatusUtil.countDownCoroutines
import com.example.servicecreate.MainActivity
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.ActivityAuthBinding
import com.example.servicecreate.logic.network.model.LoginResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Job
import retrofit2.Call


class AuthActivity : BaseActivity<ActivityAuthBinding>() , AuthListener{

    private val mViewModel: AuthViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AuthViewModel::class.java)
    }

    private var mCountdownJob: Job? = null

    override fun ActivityAuthBinding.initBindingView() {
        viewModel = mViewModel
        mViewModel.authListener = this@AuthActivity

        //Animation
        val transition = LayoutTransition()
        transition.setInterpolator(Transition.MATCH_ID, LinearInterpolator())
        authConstraint.layoutTransition = transition

        authTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab?.text?.equals(getString(R.string.auth_tab1)) == true) {
                    authCard.visibility = View.GONE
                    mViewModel.login = true
                    getString(R.string.auth_tab1).also { authBtLogin.text = it }
                } else {
                    authCard.visibility = View.VISIBLE
                    authBtLogin.text = getString(R.string.auth_tab2)
                    mViewModel.login = false
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }



        })

        authEdVerify.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0?.length == 4){
                    mViewModel.onCheckCode()
                }
            }

        })

        authBtSendCode.setOnClickListener {
            mViewModel.onSendCodeButtonClick()
        }

        authBtLogin.setOnClickListener {
            mViewModel.onRegisterButtonClick()
        }
    }

    override fun onLoginStarted() {

    }

    override fun onLoginSuccess(loginResult: LiveData<Result<LoginResponse>>) {
        loginResult.observe(this){result ->
            val response = result.getOrNull()
            if(response != null){
                if(response.code == 1){
                    toast(R.string.auth_verified_login_success)
                    saveLoginStatus(userId = response.data, isLogin = true, isStore = false)
                    MainActivity.startActivity(this@AuthActivity)
                    finish()
                }else{
                    response.msg?.let { toast(it) }
                }
            }else{
                toast(R.string.auth_verified_login_failure)
            }
        }
    }

    override fun onLoginFailure(msg: Int) {
        toast(msg)
    }

    override fun onRegisterStarted() {

    }

    override fun onRegisterSuccess(registerResult: LiveData<Result<SendVerifiedResponse>>) {
        registerResult.observe(this){result ->
            val response = result.getOrNull()
            if(response != null){
                if(response.code == 1){
                    binding.authTablayout.getTabAt(0)?.select()
                    registerOverUIChange()
                    toast(R.string.auth_verified_register_success)
                }else{
                    toast(response.data + response.msg)
                }
            }else{
                toast(R.string.auth_verified_register_failure)
            }
        }
    }

    override fun onRegisterFailure(msg: Int) {
        toast(msg)
    }

    override fun onVerifySuccess(sendResult: LiveData<Result<SendVerifiedResponse>>) {

//        loginResult.observe(this) { result ->
//            val response = result.getOrNull()
//            if (response != null && response.code == 200) {
//                toast(response.msg)
//                thread {
//                    mViewModel.saveUserData(response.data)
//                }
//                saveLoginStatus(response.data.id, true, true)
//                MainActivity.startActivity(this@LoginActivity)
//            } else {
//                response?.msg?.let { toast(it) }
//            }
//        }
        downTimer()
        sendResult.observe(this){ result ->
            val response = result.getOrNull()
            if(response != null){
                toast(response.data + response.msg)
            }else{
                toast("验证码发送失败")
                mCountdownJob?.cancel()
            }
        }
    }

    override fun onCheckCodeSuccess(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if(response != null){

                when (response.code) {
                    0 -> {
                        toast(R.string.auth_verified_enter_tip)
                    }
                    else -> {
                        toast(R.string.auth_verified_success_phone)
                        verifiedUIChange()
                    }
                }

            }else{
                toast(R.string.auth_verified_enter_tip)
            }
        }
    }

    override fun onVerifyFailure(msg: Int) {
        super.onVerifyFailure(msg)
        toast(msg)
        mCountdownJob?.cancel()
    }

    private fun verifiedUIChange() {
        binding.authEdVerify.visibility = View.GONE
        binding.authEdPhone.visibility = View.VISIBLE
        binding.authBtSendCode.visibility = View.GONE
    }

    private fun registerOverUIChange() {
        binding.authEdVerify.visibility = View.VISIBLE
        binding.authEdPhone.visibility = View.GONE
        binding.authBtSendCode.visibility = View.VISIBLE
    }

    /**
     * Timer in button
     */
    private fun downTimer(){
        mCountdownJob = countDownCoroutines(30, lifecycleScope,
            onTick = { second ->
                binding.authBtSendCode.text = "${second}s"
            }, onStart = {
                binding.authBtSendCode.isClickable = false
                // 倒计时开始
            }, onFinish = {
                // 倒计时结束，重置状态
                binding.authBtSendCode.text  = "发送"
                binding.authBtSendCode.isClickable = true
            })
    }

    /**
     * Save login state in sp
     */
    private fun saveLoginStatus(userId: String, isLogin: Boolean, isStore: Boolean) {
        ServiceCreateApplication.sp.edit().apply {
            clear()
            putString(ServiceCreateApplication.userID, userId)
            putBoolean(ServiceCreateApplication.isLogin, isLogin)
            putBoolean(ServiceCreateApplication.isStore, isStore)
            apply()
        }
    }

    /*
    To launch this activity
     */
    companion object{
        fun startActivity(context: Context){
            val intent = Intent(context, AuthActivity::class.java)
            context.startActivity(intent)
        }
    }

}