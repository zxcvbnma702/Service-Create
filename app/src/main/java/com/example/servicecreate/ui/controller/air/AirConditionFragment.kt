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
import androidx.lifecycle.ViewModelProvider
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentAirConditionBinding
import com.example.servicecreate.ui.home.HomeViewModel
import com.google.android.material.tabs.TabLayout
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlin.math.roundToInt

class AirConditionFragment : BaseFragment<FragmentAirConditionBinding>(), AirConditionListener{

    private val mViewModel: AirConditionViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AirConditionViewModel::class.java)
    }

    override fun FragmentAirConditionBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.airConditionListener = this@AirConditionFragment

        val transition = LayoutTransition()
        transition.setInterpolator(Transition.MATCH_ID, LinearInterpolator())
        airLayout.layoutTransition = transition

        /**
         * Air Temperature Adjustment
         */
        with(airCirSeekbar){
            setOnProgressChangedListener{progress ->
                airCirProgress.text = progress.roundToInt().toString()
                airCirProgressInner.progress = progress
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
                        0-> { Toast.makeText(this@AirConditionFragment.requireContext(), tab.text, Toast.LENGTH_SHORT).show()}
                        1-> { Toast.makeText(this@AirConditionFragment.requireContext(), tab.text, Toast.LENGTH_SHORT).show()}
                        2-> { Toast.makeText(this@AirConditionFragment.requireContext(), tab.text, Toast.LENGTH_SHORT).show()}
                        3-> { Toast.makeText(this@AirConditionFragment.requireContext(), tab.text, Toast.LENGTH_SHORT).show()}
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }

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
                            Log.e("speed", "关闭")
                        }
                        33 -> {   Log.e("speed", "1dang") }
                        67 -> {Log.e("speed", "2dang")}
                        100 -> {Log.e("speed", "3dang")}
                    }
                }
            })
        }

    }

}