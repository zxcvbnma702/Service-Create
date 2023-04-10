package com.example.servicecreate.ui.controller.doorlock

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ethanco.lib.PasswordDialog
import com.ethanco.lib.abs.ICheckPasswordFilter
import com.ethanco.lib.abs.OnPositiveButtonListener
import com.example.base.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentDoorLockBinding


class DoorLockFragment(id: Long) : BaseFragment<FragmentDoorLockBinding>() {

    private lateinit var builder: PasswordDialog.Builder
    private val mViewModel: DoorLockViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DoorLockViewModel::class.java]
    }

    override fun FragmentDoorLockBinding.initBindingView() {
        this.viewModel = mViewModel

        initDialog()
        doorlockPasswordInputFirst.setOnClickListener {
            builder.create().show()
        }
    }

    private fun initDialog() {
        builder = PasswordDialog.Builder(requireContext())
            .setTitle(R.string.please_input_password) //Dialog标题
            //.setBoxCount(4) //设置密码位数，此方法有BUG，建议在Builder构造函数中传入layoutRes来定义
            .setBorderNotFocusedColor(R.color.main_color) //边框颜色
            .setDotNotFocusedColor(R.color.main_color_primary_secondary) //密码圆点颜色
            .setPositiveListener(OnPositiveButtonListener { dialog, which, text ->
                //确定
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            })
            .setNegativeListener { _, i ->
                //取消
                Toast.makeText(requireContext(), "取消", Toast.LENGTH_SHORT).show()
            }
            .addCheckPasswordFilter(CountCheckFilter()) //添加过滤器
    }

    inner class CountCheckFilter : ICheckPasswordFilter {
        override fun onCheckPassword(context: Context?, password: CharSequence): Boolean {
            if (password.length != 4) {
                Toast.makeText(context, "密码需为4位", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }
    }


}