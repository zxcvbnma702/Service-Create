package com.example.base.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.base.`interface`.BaseBinding
import com.example.base.kxt.getViewBinding
import com.example.base.kxt.toast
import com.example.base.ui.util.StatusUtil

/**
 * @author:SunShibo
 * @date:2022-08-22 23:01
 * @feature:
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), BaseBinding<VB> {
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            for ((name, granted) in isGranted) {
                if (!granted) {
                    toast("${name}权限请求失败")
                }
            }
        }

    private val binding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewBinding(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.initBindingView()
        initPermission()
        initStatusBsr()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initStatusBsr() {
        StatusUtil.initActivityBar(this@BaseActivity, true)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initPermission() {
        requestPermissions.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

}