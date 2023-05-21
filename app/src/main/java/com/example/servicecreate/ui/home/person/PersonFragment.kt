package com.example.servicecreate.ui.home.person

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.base.activity.BaseFragment
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentPersonBinding
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.home.gateway.GatewayViewModel
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.OnBindView

class PersonFragment : BaseFragment<FragmentPersonBinding>() , PersonListener{

    private lateinit var changeDialog: MessageDialog
    internal val mViewModel: PersonViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[PersonViewModel::class.java]
    }

    override fun FragmentPersonBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.personListener = this@PersonFragment

        personToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        personChangePassword.setOnClickListener {
            changeDialog = MessageDialog.show(
                "修改密码",
                "",
                "确定",
                "取消",
                "发送验证码"
            )
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setCustomView(object: OnBindView<MessageDialog>(R.layout.item_change_password_view){
                    override fun onBind(dialog: MessageDialog?, v: View?) {
                        val oldPwd = v?.findViewById<EditText>(R.id.custom_input_oldpwd)
                        oldPwd?.doAfterTextChanged {
                            mViewModel.oldPwd = it.toString()
                        }
                        val newPwd = v?.findViewById<EditText>(R.id.custom_input_newpwd)
                        newPwd?.doAfterTextChanged {
                            mViewModel.newPwd = it.toString()
                        }
                        val code = v?.findViewById<EditText>(R.id.custom_input_verified)
                        code?.doAfterTextChanged {
                            mViewModel.verifiedCode = it.toString()
                        }
                    }
                })
                .setOtherTextInfo(dialogCancelInfo(requireContext()))
                .setCancelTextInfo(dialogCancelInfo(requireContext()))
                .setOkButton { dialog, v ->
                    mViewModel.changePassword()
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
    }

    override fun onChangeOver(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(response.msg + response.data)
                    changeDialog.dismiss()
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