package com.example.servicecreate.ui.home.wisdom

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.base.kxt.toast
import com.example.base.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentWisdomBinding
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.dialogMessageInfo
import com.example.servicecreate.ui.dialogOkInfo
import com.example.servicecreate.ui.dialogTitleInfo
import com.kongzue.dialogx.dialogs.MessageDialog

class WisdomFragment : BaseFragment<FragmentWisdomBinding>(), WisdomListener {
    private val mViewModel: WisdomViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[WisdomViewModel::class.java]
    }

    override fun FragmentWisdomBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.wisdomListener = this@WisdomFragment

        wisdomToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        wisdomIndoor.setOnClickListener {
            MessageDialog.show(getString(R.string.wisdom_indoor_title), getString(R.string.wisdom_indoor_content), "确定")
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setTitleTextInfo(dialogTitleInfo(requireContext()))
                .setOkTextInfo(dialogOkInfo(requireContext()))
                .setMessageTextInfo(dialogMessageInfo(requireContext()))
                .setOkButton { _, _ ->
                    mViewModel.controllerIndoor()
                    false

                }
        }

        wisdomOutdoor.setOnClickListener {
            MessageDialog.show(getString(R.string.wisdom_outdoor_title), getString(R.string.wisdom_outdoor_content), "确定")
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setTitleTextInfo(dialogTitleInfo(requireContext()))
                .setOkTextInfo(dialogOkInfo(requireContext()))
                .setMessageTextInfo(dialogMessageInfo(requireContext()))
                .setOkButton { _, _ ->
                    mViewModel.controllerOutdoor()
                    false
                }
        }

        wisdomSleep.setOnClickListener {
            MessageDialog.show(getString(R.string.wisdom_sleep_title), getString(R.string.wisdom_sleep_content), "确定")
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setTitleTextInfo(dialogTitleInfo(requireContext()))
                .setOkTextInfo(dialogOkInfo(requireContext()))
                .setMessageTextInfo(dialogMessageInfo(requireContext()))
                .setOkButton { _, _ ->
                    mViewModel.controllerSleep()
                    false
                }
        }

    }

    override fun onIndoorController(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("Indoor"+ response.msg + response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onOutdoorController(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("OutDoor" +response.msg + response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onSleepController(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("Sleep" + response.msg + response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }
}