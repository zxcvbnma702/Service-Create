package com.example.servicecreate.ui.controller.light

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentLightBinding
import com.example.servicecreate.ui.controller.air.AirConditionViewModel

class LightFragment : BaseFragment<FragmentLightBinding>(), LightListener{
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

        lightSwitch.setOnCheckedChangeListener { p0, p1 ->
            if (!p1) {
                lightAnim.cancelAnimation()
            } else {
                lightAnim.playAnimation()
            }
        }
    }


}