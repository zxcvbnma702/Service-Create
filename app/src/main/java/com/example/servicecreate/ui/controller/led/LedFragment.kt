package com.example.servicecreate.ui.controller.led

import com.example.base.activity.BaseFragment
import com.example.servicecreate.databinding.FragmentLedBinding
import com.example.servicecreate.ui.toast

class LedFragment : BaseFragment<FragmentLedBinding>() {
    override fun FragmentLedBinding.initBindingView() {
        colorPicker.setBrightnessGradientView(brightPicker)
        colorPicker.setOnColorChangedListener { gradientView, color ->
//            Log.e("color", Integer.toHexString(color))
            colorButton.highlightColor = color
        }
        brightPicker.setOnColorChangedListener { gradientView, color ->
//            Log.e("brcolor", Integer.toHexString(color))
            led.setColorFilter(color)
            colorButton.setTextColor(color)
        }

        colorButton.setOnClickListener {
            "没有接口".toast()
        }
    }

}