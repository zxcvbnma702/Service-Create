package com.example.servicecreate.ui.append

import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.lifecycle.ViewModelProvider
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentAppendBinding
import com.example.servicecreate.logic.db.model.AppendItem
import com.example.servicecreate.ui.append.adapter.AppendDefaultRecyclerAdapter
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
    private lateinit var defaultAdapter: AppendDefaultRecyclerAdapter
    private lateinit var netAdapter: AppendDefaultRecyclerAdapter
    private var check: Boolean = false

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

        initView()

        return binding.root
    }


    private fun initView() {

        val transition = LayoutTransition()
        transition.setInterpolator(Transition.MATCH_ID, LinearInterpolator())

        with(binding){
            designBottomSheet.layoutTransition = transition

            defaultAdapter = AppendDefaultRecyclerAdapter(this@AppendFragment)
            netAdapter = AppendDefaultRecyclerAdapter(this@AppendFragment)
            appendRecycler.adapter = defaultAdapter

            defaultAdapter.setData(
                listOf(
                    AppendItem(2, R.drawable.ic_device_air, "空调"),
                    AppendItem(3, R.drawable.ic_device_door_lock, "智能门锁"),
                    AppendItem(1, R.drawable.ic_device_lamp, "灯"),
                    AppendItem(4,R.drawable.ic_device_air, "空调"),
                    AppendItem(4, R.drawable.ic_device_air, "空调"),
                    AppendItem(0, R.drawable.ic_room_10, "添加房间")
            ))

            appendSearch.setOnClickListener {
                if(check){
                    appendSearchAnim.visibility = View.GONE
                    appendSearchAnim.cancelAnimation()
                    appendSearchTip.text = getString(R.string.append_search_tip)
                    appendSearch.text = getString(R.string.append_search)
                    appendRecycler.adapter = defaultAdapter
                    check = ! check
                }else{
                    appendSearchAnim.visibility = View.VISIBLE
                    appendSearchAnim.playAnimation()
                    appendSearchTip.text = getString(R.string.append_search_running)
                    appendSearch.text = getString(R.string.append_search_cancel)
                    appendRecycler.adapter = netAdapter
                    check = ! check
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        initBottomSheet()
    }

    //初始化bottomSheet状态
    private fun initBottomSheet() {
        val view: View = dialog?.findViewById(R.id.design_bottom_sheet)!!
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        behavior = BottomSheetBehavior.from(view)
        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = false
        }
    }
}