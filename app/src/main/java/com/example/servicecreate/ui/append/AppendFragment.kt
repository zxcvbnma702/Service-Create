package com.example.servicecreate.ui.append

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentAppendBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author:SunShibo
 * @date:2023-03-21 21:54
 * @feature:
 */
class AppendFragment: BottomSheetDialogFragment(), AppendListener {
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var binding: FragmentAppendBinding

    internal val viewModel: AppendViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AppendViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogBg)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppendBinding.inflate(inflater)
        binding.viewModel = viewModel
        viewModel.appendListener = this@AppendFragment

        return binding.root
    }

    override fun onStart() {
        super.onStart()
//        initBottomSheet()
    }

    //初始化bottomSheet状态
    private fun initBottomSheet() {
        val view: View = dialog?.findViewById(R.id.append_bottom_sheet)!!
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        behavior = BottomSheetBehavior.from(view)
        behavior.apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
            isDraggable = false
        }
    }
}