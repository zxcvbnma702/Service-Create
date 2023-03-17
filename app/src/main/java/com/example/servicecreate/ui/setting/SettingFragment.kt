package com.example.servicecreate.ui.setting

import androidx.lifecycle.ViewModelProvider
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding>(), SettingListener {

    private val mViewModel: SettingViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SettingViewModel::class.java)
    }

    override fun FragmentSettingBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.settingListener = this@SettingFragment
    }

}