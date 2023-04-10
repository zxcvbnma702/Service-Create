package com.example.servicecreate.ui.home.setting

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.databinding.FragmentSettingBinding
import com.example.servicecreate.ui.auth.AuthActivity
import com.example.servicecreate.ui.home.MainListener
import com.example.servicecreate.ui.home.home.HomeViewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>(), MainListener {

    private val mViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]
    }

    override fun FragmentSettingBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.mainListener = this@SettingFragment

        settingGateway.setOnClickListener {
            mViewModel.jumpToGateWayPage(1)
        }

        settingWisdom.setOnClickListener {
            mViewModel.jumpToWisdomPage(1)
        }

        settingLogout.setOnClickListener {
            AuthActivity.startActivity(requireContext())
            requireActivity().finish()
        }
    }

}