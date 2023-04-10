package com.example.servicecreate.ui.home.append

import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentAppendBinding
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.home.adapter.AppendDefaultRecyclerAdapter
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

    internal val mViewModel: AppendViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[AppendViewModel::class.java]
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
        binding.viewModel = mViewModel
        mViewModel.appendListener = this@AppendFragment

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
                    DeviceData("", 0L, 0, "添加房间", mutableListOf(), 0, "")
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
                    mViewModel.findDevice()
                    appendRecycler.adapter = netAdapter
                    check = ! check
                }
            }
        }
        mViewModel.getRoomList()
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
            isDraggable = true
        }
    }

    override fun onAddRoom(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val responses = re.getOrNull()
            if (responses != null) {
                when(responses.code){
                    1-> {
                        requireContext().toast(responses.data)
                        senResult()
                        behavior.apply {
                            state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }
                    else ->{
                        requireContext().toast(responses.msg)
                    }
                }
            }
        }
    }

    override fun onGetRoomList(result: LiveData<Result<RoomListResponse>>) {
        result.observe(this){ re ->
            val responses = re.getOrNull()
            if (responses != null) {
                netAdapter.roomList.putAll(responses.data.associate {
                        it.name to it.id
                    })
                }
            }

    }

    override fun onAddDeviceToRoom(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val responses = re.getOrNull()
            if (responses != null) {
                when(responses.code){
                    1-> {
                        requireContext().toast(responses.data)
                        mViewModel.findDevice()
                    }
                    else ->{
                        requireContext().toast(responses.msg)
                    }
                }
            }
        }
    }

    override fun onFindDevice(result: LiveData<Result<DeviceListResponse>>) {
        result.observe(this){ re ->
            val responses = re.getOrNull()
            if (responses != null) {
                when(responses.code){
                    1-> {
                        netAdapter.setData(responses.data)
                    }
                    else ->{
                        responses.msg?.let { requireContext().toast(it) }
                    }
                }
            }
        }
    }

    private fun senResult(){
        val result = "result"
        setFragmentResult("refreshKey", bundleOf("bundleKey" to result))
    }
}