package com.example.base.`interface`

import androidx.databinding.ViewDataBinding

/**
 * @author:SunShibo
 * @date:2022-09-26 22:25
 * @feature: viewBinding interface
 */
interface BaseBinding<VB : ViewDataBinding> {
    fun VB.initBindingView()
}