package com.example.servicecreate.ui.controller.air

import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.kxt.toast
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentAirConditionBinding
import com.example.servicecreate.logic.network.model.AirDetailResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.toast
import com.google.android.material.tabs.TabLayout
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.seosh817.circularseekbar.callbacks.OnAnimationEndListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class AirConditionFragment(private val id: Long) : BaseFragment<FragmentAirConditionBinding>(), AirConditionListener{

    private val mViewModel: AirConditionViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[AirConditionViewModel::class.java]
    }

    override fun FragmentAirConditionBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.airConditionListener = this@AirConditionFragment

        val transition = LayoutTransition()
        transition.setInterpolator(Transition.MATCH_ID, LinearInterpolator())
        airLayout.layoutTransition = transition

        initData()

        with(mViewModel){
            lifecycleScope.launch {
                _airController.collect{
                    mViewModel.sendControllerToNet()
                    Log.e("air", it.toString())
                }
            }
        }

        /**
         * Air Switch
         */
        airSwitch.setOnCheckedChangeListener { _, b ->
            if(b){
                mViewModel.airControllerData.state = 1
                mViewModel.sendAirController()
            }else{
                mViewModel.airControllerData.state = 0
                mViewModel.sendAirController()
            }
        }

        /**
         * Air Temperature Adjustment
         */
        with(airCirSeekbar){
            setOnProgressChangedListener{progress ->
                airCirProgress.text = progress.roundToInt().toString()
                airCirProgressInner.progress = progress
                mViewModel.airControllerData.temp = progress.toInt()
            }
            setOnAnimationEndListener {
                mViewModel.sendAirController()
            }
            airTemperatureAdd.setOnClickListener {
                this.progress = this.progress.plus(1)
            }
            airTemperatureSub.setOnClickListener {
                this.progress = this.progress.minus(1)
            }
        }

        /**
         * Air Mode Shift
         */
        with(airModeShift){
            addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        0-> {
                            mViewModel.airControllerData.mode = 0
                            mViewModel.sendAirController()
                        }
                        1-> {
                            mViewModel.airControllerData.mode = 1
                            mViewModel.sendAirController()
                        }
                        2-> {
                            mViewModel.airControllerData.mode = 2
                            mViewModel.sendAirController()
                        }
                        3-> {
                            mViewModel.airControllerData.mode = 3
                            mViewModel.sendAirController()
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }

        /**
         * Air Wind Speed Shift
         */
        with(airWindSpeed){
            setIndicatorText(getString(R.string.air_speed_tip))
            setOnRangeChangedListener(object: OnRangeChangedListener{
                override fun onRangeChanged(
                    view: RangeSeekBar?,
                    leftValue: Float,
                    rightValue: Float,
                    isFromUser: Boolean
                ) = if (leftValue.roundToInt() <= 32) {
                    airWindShift.visibility = View.GONE
                } else {
                    airWindShift.visibility = View.VISIBLE
                }

                override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

                }

                override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                    when(view?.leftSeekBar?.progress?.roundToInt()){
                        0 ->
                        {
                            mViewModel.airControllerData.grade = 0
                            mViewModel.sendAirController()
                        }
                        33 -> {
                            mViewModel.airControllerData.grade = 1
                            mViewModel.sendAirController()
                        }
                        67 -> {
                            mViewModel.airControllerData.grade = 2
                            mViewModel.sendAirController()
                        }
                        100 -> {
                            mViewModel.airControllerData.grade = 3
                            mViewModel.sendAirController()
                        }
                    }
                }
            })
        }

        /**
         * Air Wind Direction Shift
         */
        with(airWindShift){
            addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        0-> {
                            mViewModel.airControllerData.scaveng = 0
                            mViewModel.sendAirController()
                        }
                        1-> {
                            mViewModel.airControllerData.scaveng = 1
                            mViewModel.sendAirController()
                        }
                        2-> {
                            mViewModel.airControllerData.scaveng = 2
                            mViewModel.sendAirController()
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }

    }

    private fun initData() {
        mViewModel.id = id.toInt()
        mViewModel.airControllerData.id = id.toInt()
        mViewModel.getAirDetail(id.toInt().absoluteValue)
        Log.e("airDeviceId", id.toString())
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

    override fun onAirDetailData(result: LiveData<Result<AirDetailResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    with(binding){
                        airSwitch.isChecked = response.data.state == 1
                        airCirProgress.text = response.data.temp.toString()
                        airCirSeekbar.progress = response.data.temp.toFloat()
                        airModeShift.getTabAt(response.data.mode)?.select()
                        when(response.data.grade){
                            1 -> airWindSpeed.setProgress(33f)
                            2 -> airWindSpeed.setProgress(67f)
                            3 -> airWindSpeed.setProgress(100f)
                            else -> airWindSpeed.setProgress(0f)
                        }
                        airWindShift.getTabAt(response.data.scaveng)?.select()
                    }
                }else{
                    "未知错误".toast()
                }
            }
        }
    }

}