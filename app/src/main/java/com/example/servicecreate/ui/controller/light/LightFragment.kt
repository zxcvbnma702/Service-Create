package com.example.servicecreate.ui.controller.light

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.kxt.toast
import com.example.base.activity.BaseFragment
import com.example.servicecreate.databinding.FragmentLightBinding
import com.example.servicecreate.logic.network.model.LampDetailResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.toast
import com.google.android.exoplayer2.util.Log
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class LightFragment(private val id: Long, private val roomName: String) : BaseFragment<FragmentLightBinding>(), LightListener{
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

        lightDeviceRoom.text = roomName

        lightSwitch.setOnCheckedChangeListener { p0, p1 ->
            if (!p1) {
                mViewModel.state = 0
                mViewModel.sendLampController()
                lightDeviceName.text = "电源: 关"
                lightAnim.cancelAnimation()
            } else {
                mViewModel.state = 1
                mViewModel.sendLampController()
                lightDeviceName.text = "电源: 开"
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
                    requireContext().toast(response.data)
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
                        if(!lightSwitch.isChecked){
                            lightDeviceName.text = "电源: 关"
                        }
                    }
                }else{
                    "灯信息初始化失败".toast()
                }
            }
        }
    }
}