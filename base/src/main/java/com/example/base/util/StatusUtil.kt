package com.example.base.ui.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.base.kxt.toast
import com.example.base.util.NetWorkReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

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
            WindowCompat.setDecorFitsSystemWindows(window, isImmerse)

            val params = window.attributes
            params.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
    }

    fun countDownCoroutines(
        total: Int,
        scope: CoroutineScope,
        onTick: (Int) -> Unit,
        onStart: (() -> Unit)? = null,
        onFinish: (() -> Unit)? = null,
    ): Job {
        return flow {
            for (i in total downTo 0) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Main)
            .onStart { onStart?.invoke() }
            .onCompletion { onFinish?.invoke() }
            .onEach { onTick.invoke(it) }
            .launchIn(scope)
    }

    fun checkNetWork(context: Context){
        when(NetWorkReceiver.getNetWorkState(context)){
            -1 -> context.toast("网络状态异常, 请检查网络连接")
        }
    }
}