package com.example.servicecreate.ui.controller.light

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.kxt.toast
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentLightBinding
import com.example.servicecreate.logic.network.model.LampDetailResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.controller.air.AirConditionViewModel
import com.example.servicecreate.ui.toast
import com.google.android.exoplayer2.util.Log
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class LightFragment(private val id: Long) : BaseFragment<FragmentLightBinding>(), LightListener{
    private val mViewModel: LightViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[LightViewModel::class.java]
    }

    override fun FragmentLightBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.lightListener = this@LightFragment

        if(lightSwitch.isChecked){
            lightAnim.playAnimation()
        }

        initData()

        lightSwitch.setOnCheckedChangeListener { p0, p1 ->
            if (!p1) {
                mViewModel.state = 0
                mViewModel.sendLampController()
                lightAnim.cancelAnimation()
            } else {
                mViewModel.state = 1
                mViewModel.sendLampController()
                lightAnim.playAnimation()
            }
        }

        with(mViewModel){
            lifecycleScope.launch{
                _lampController.collect{
                    mViewModel.sendControllerToNet()
                    Log.e("lampState", mViewModel.state.toString())
                }
            }
        }
    }

    private fun initData() {
        mViewModel.id = id.toInt()
        mViewModel.getLampDetail(id.toInt().absoluteValue)
        Log.e("lampDeviceId", id.toString())
    }

    override fun onSendControllerData(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(response.msg + response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onLampDetailData(result: LiveData<Result<LampDetailResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    with(binding){
                        lightSwitch.isChecked = response.data.state == 1
                    }
                }else{
                    "灯信息初始化失败".toast()
                }
            }
        }
    }
}