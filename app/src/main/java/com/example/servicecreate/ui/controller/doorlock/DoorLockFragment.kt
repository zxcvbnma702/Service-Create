package com.example.servicecreate.ui.controller.doorlock

import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ethanco.lib.PasswordInput
import com.example.base.activity.BaseFragment
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentDoorLockBinding
import com.example.servicecreate.logic.network.model.DoorLockDetailResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.OnBindView
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


class DoorLockFragment(private val id: Long, private val roomName: String) : BaseFragment<FragmentDoorLockBinding>(), DoorLockListener {

    private lateinit var pawdDialog: MessageDialog
    private val mViewModel: DoorLockViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DoorLockViewModel::class.java]
    }

    override fun FragmentDoorLockBinding.initBindingView() {
        this.viewModel = mViewModel
        mViewModel.doorLockListener = this@DoorLockFragment

        initData()

        doorlockDeviceRoom.text = roomName

        doorlockSwitch.setOnCheckedChangeListener { p0, p1 ->
            if (!p1) {
                mViewModel.state = 0
                doorlockDeviceName.text = "电源: 关"
                mViewModel.sendDoorLockController()
            } else {
                mViewModel.state = 1
                doorlockDeviceName.text = "电源: 开"
                mViewModel.sendDoorLockController()
            }
        }

        doorlockPasswordInput.setOnClickListener {
            pawdDialog = MessageDialog.show(
                getString(R.string.please_input_password),
                "",
                "确定",
                "取消",
                "发送验证码 "
            )
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setCustomView(object : OnBindView<MessageDialog>(R.layout.item_door_lock_dialog) {
                    override fun onBind(dialog: MessageDialog?, v: View?) {
                        val pawd = v?.findViewById<EditText>(R.id.custom_input_pwd)
                        pawd?.doAfterTextChanged {
                            mViewModel.newPawd = it.toString()
                        }
                        val code = v?.findViewById<EditText>(R.id.custom_input_code)
                        code?.doAfterTextChanged {
                            mViewModel.code = it.toString()
                        }
                    }
                })
                .setCancelTextInfo(dialogCancelInfo(requireContext()))
                .setOkButton { dialog, v ->
                    if (mViewModel.checkPawd()) {
                        mViewModel.changePawd()
                    }
                    true
                }
                .setOtherButton { dialog, v ->
                    mViewModel.sendCommonVerified()
                    true
                }
                .setCancelButton { _, _ ->
                    false
                }
        }

        with(mViewModel) {
            lifecycleScope.launch {
                _doorLockController.collect {
                    mViewModel.sendControllerToNet()
                    Log.e("doorLockState", mViewModel.state.toString())
                }
            }
        }
    }

    private fun initData() {
        mViewModel.id = id.toInt()
        mViewModel.getDoorLockDetail(id.toInt().absoluteValue)
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

    override fun onDoorLockDetailData(result: LiveData<Result<DoorLockDetailResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    with(binding){
                        doorlockSwitch.isChecked = response.data.status == 1
                        if(!doorlockSwitch.isChecked) {
                            doorlockDeviceName.text = "电源: 关"
                        }
                        doorlockPassword.text = response.data.password
                    }
                }else{
                    "门锁信息初始化失败".toast()
                }
            }
        }
    }

    override fun onChangePawd(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(response.data)
                    pawdDialog.dismiss()
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onSendVerified(result: LiveData<Result<SendVerifiedResponse>>) {
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
}