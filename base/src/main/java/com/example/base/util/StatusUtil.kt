package com.example.base.ui.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment

/**
 * @author:SunShibo
 * @date:2022-10-07 16:07
 * @feature:Adjust the system view status
 */
object StatusUtil {

    @RequiresApi(Build.VERSION_CODES.P)
    fun initFragmentBar(context: Fragment, isImmerse: Boolean) {
        context.apply {
            requireActivity().window.statusBarColor = Color.TRANSPARENT

            val controller = WindowInsetsControllerCompat(
                requireActivity().window,
                requireActivity().window.decorView
            )
            controller.apply {
                isAppearanceLightStatusBars = false
                hide(WindowInsetsCompat.Type.statusBars())
            }
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, isImmerse)

            val params = requireActivity().window.attributes
            params.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            requireActivity().window.attributes = params
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun initActivityBar(context: Activity, isImmerse: Boolean) {
        context.apply {
            window.statusBarColor = Color.TRANSPARENT

            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.apply {
                isAppearanceLightStatusBars = false
                hide(WindowInsetsCompat.Type.statusBars())
            }
            WindowCompat.setDecorFitsSystemWindows(window, true)

            val params = window.attributes
            params.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
    }
}