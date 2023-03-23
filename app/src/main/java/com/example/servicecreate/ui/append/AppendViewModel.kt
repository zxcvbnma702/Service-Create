package com.example.servicecreate.ui.append

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * @author:SunShibo
 * @date:2023-03-21 21:55
 * @feature:
 */
class AppendViewModel: ViewModel() {
    internal var appendListener: AppendListener ?= null
}