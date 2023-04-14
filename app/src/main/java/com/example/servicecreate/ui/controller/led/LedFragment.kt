package com.example.servicecreate.ui.controller.led

import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.activity.BaseFragment
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentLedBinding
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.launch


class LedFragment(private val id: Long, private val roomName: String) : BaseFragment<FragmentLedBinding>(), LedListener {

    private val mViewModel: LedViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[LedViewModel::class.java]
    }

    override fun FragmentLedBinding.initBindingView() {
        this.viewModel = mViewModel
        mViewModel.ledListener = this@LedFragment

        initData()

        ledDeviceRoom.text = roomName

        ledSwitch.setOnCheckedChangeListener { p0, p1 ->
            if (!p1) {
                mViewModel.state = 0
                ledDeviceName.text = "电源: 关"
                mViewModel.sendLedController()
            } else {
                mViewModel.state = 1
                ledDeviceName.text = "电源: 开"
                mViewModel.sendLedController()
            }
        }

        ledChangeButton.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setTitle("颜色选择")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.confirm),
                    ColorEnvelopeListener { envelope, fromUser ->
                        mViewModel.sendControllerToNet(envelope.argb[0], envelope.argb[1], envelope.argb[2], envelope.argb[3])
                    })
                .setNegativeButton(getString(R.string.cancel)
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }

        with(mViewModel){
            lifecycleScope.launch{
                _ledController.collect{
                    mViewModel.sendStateControllerToNet()
                    Log.e("ledState", mViewModel.state.toString())
                }
            }
        }
    }

    private fun initData() {
        mViewModel.id = id.toInt()
//        mViewModel.getLedDetail(id.toInt().absoluteValue)
    }

    override fun onSendColorControllerData(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("颜色修改: " + response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onSendStateControllerData(result: LiveData<Result<SendVerifiedResponse>>) {
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
}