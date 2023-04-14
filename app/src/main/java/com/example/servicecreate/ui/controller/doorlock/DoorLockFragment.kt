package com.example.servicecreate.ui.controller.doorlock

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ethanco.lib.PasswordDialog
import com.ethanco.lib.PasswordInput
import com.ethanco.lib.abs.ICheckPasswordFilter
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
                " "
            )
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setCustomView(object: OnBindView<MessageDialog>(R.layout.doorlock_dialog){
                    override fun onBind(dialog: MessageDialog?, v: View?) {
                        val pawd = v?.findViewById<PasswordInput>(R.id.pwdInput_dialog)
                        pawd?.doAfterTextChanged {
                            mViewModel.newPawd = it.toString()
                        }
                    }
                })
                .setCancelTextInfo(dialogCancelInfo(requireContext()))
                .setOkButton { dialog, v ->
                    Log.e("pawd", mViewModel.newPawd)
                    if(mViewModel.checkPawd()){
                        mViewModel.changePawd()
                    }
                    true
                }
                .setCancelButton { _, _ ->
                    false
                }
        }

        with(mViewModel){
            lifecycleScope.launch{
                _doorLockController.collect{
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
}