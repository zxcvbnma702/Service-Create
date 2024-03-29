package com.example.servicecreate.ui.home.append

import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentAppendBinding
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.*
import com.example.servicecreate.ui.auth.AuthActivity
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.dialogMessageInfo
import com.example.servicecreate.ui.dialogOkInfo
import com.example.servicecreate.ui.dialogTitleInfo
import com.example.servicecreate.ui.home.adapter.AppendDefaultRecyclerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.OnBindView

/**
 * @author:SunShibo
 * @date:2023-03-21 21:54
 * @feature:
 */
class AppendFragment(private val i: Int) : BottomSheetDialogFragment(), AppendListener {
    private lateinit var inputDialog: MessageDialog
    private lateinit var macDialog: MessageDialog
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var binding: FragmentAppendBinding
    private lateinit var defaultAdapter: AppendDefaultRecyclerAdapter
    private lateinit var fakeAdapter: AppendDefaultRecyclerAdapter
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
            fakeAdapter = AppendDefaultRecyclerAdapter(this@AppendFragment)
            //假的adapter
            fakeAdapter.setData(
                listOf(
                    DeviceData("", 0L, 1, "空调", mutableListOf(), 1, false, ""),
                    DeviceData("", 0L, 1, "日光灯", mutableListOf(), 2, false, ""),
                    DeviceData("", 0L, 1, "智能门锁", mutableListOf(), 3, false, ""),
                    DeviceData("", 0L, 1, "Led灯", mutableListOf(), 4, false, ""),
                    DeviceData("", 0L, 1, "摄像头", mutableListOf(), 5, false, ""),
                    DeviceData("", 0L, 1, "窗帘", mutableListOf(), 6, false, ""),
                    DeviceData("", 0L, 1, "网关", mutableListOf(), 255, false, ""),
                    DeviceData("", 0L, 1, "智能厨具", mutableListOf(), 88, false, ""),
                    DeviceData("", 0L, 1, "智能开关", mutableListOf(), 89, false, ""),
                    DeviceData("", 0L, 1, "智能微波炉", mutableListOf(), 90, false, ""),
                    DeviceData("", 0L, 1, "智能插座", mutableListOf(), 91, false, ""),
                ))
            appendRecycler.adapter = defaultAdapter

            when(i){
                //添加房间
                0 ->{
                    appendSearch.visibility = View.GONE
                    appendSearchTip.text = getString(R.string.append_search_tip_room)
                    defaultAdapter.setData(
                        listOf(
                            DeviceData("", 0L, 0, "添加房间", mutableListOf(), 0, false, "")
                        ))
                }
                //添加设备
                1->{
                    appendSearchTip.text = getString(R.string.append_search_tip_device)
                    appendRecycler.adapter = fakeAdapter
                }
            }

            appendSearch.setOnClickListener {
                if(ServiceCreateApplication.sp.getBoolean(ServiceCreateApplication.isGateAay, false)){
                    if(check){
                        appendSearchAnim.visibility = View.GONE
                        appendSearchAnim.cancelAnimation()
                        appendSearchTip.text = getString(R.string.append_search_tip_device)
                        appendSearch.text = getString(R.string.append_search)
                        appendRecycler.adapter = fakeAdapter
                        check = ! check
                    }else{
                        appendSearchAnim.visibility = View.VISIBLE
                        appendSearchAnim.playAnimation()
                        appendSearchTip.text = getString(R.string.append_search_running)
                        appendSearch.text = getString(R.string.append_search_cancel)
                        appendRecycler.adapter = defaultAdapter
                        mViewModel.findDevice()
                        check = ! check
                    }
                }else{
                    MessageDialog.show(getString(R.string.append_no_gateway_title), getString(R.string.append_no_gateway_title_content), "确定")
                        .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                        .setCancelable(false)
                        .setTitleTextInfo(dialogTitleInfo(requireContext()))
                        .setOkTextInfo(dialogOkInfo(requireContext()))
                        .setMessageTextInfo(dialogMessageInfo(requireContext()))
                        .setOkButton { _, _ ->
                            false
                        }
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

    internal fun showMACDialog() {
        macDialog = MessageDialog.show(
            "请输入网关设备上的MAC地址和密码",
            "",
            "确定",
            "取消",
            " "
        )
            .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
            .setCancelable(false)
            .setCustomView(object: OnBindView<MessageDialog>(R.layout.item_extend_view){
                override fun onBind(dialog: MessageDialog?, v: View?) {
                    val deviceId = v?.findViewById<EditText>(R.id.custom_input_device_id)
                    deviceId?.doAfterTextChanged {
                        mViewModel.MACAccount =  it.toString()
                    }
                    val deviceName = v?.findViewById<EditText>(R.id.custom_input_device_name)
                    deviceName?.doAfterTextChanged {
                        mViewModel.MACPassword =  it.toString()
                    }
                }
            })
            .setCancelTextInfo(dialogCancelInfo(requireContext()))
            .setOkButton { dialog, v ->
                if(mViewModel.hasMacs()){
                    mViewModel.sendUserMac()
                }
                true
            }
            .setCancelButton { _, _ ->
                false
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
                defaultAdapter.roomList.putAll(responses.data.associate {
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
                        inputDialog.dismiss()
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
                        defaultAdapter.setData(responses.data)
//                        binding.appendSearchAnim.visibility = View.GONE
//                        binding.appendSearchAnim.cancelAnimation()
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

    override fun onSendMac(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(response.msg + response.data)
                    macDialog.dismiss()
                    //依赖后端刷新
                    mViewModel.findDevice()
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    internal fun addDevice(deviceId: String, roomId: Long?, text: CharSequence, deviceType: Int) {
        inputDialog = InputDialog.show(
            requireContext().getString(R.string.append_device_to) + " " + text,
            "请输入设备名",
            "确定",
            "取消",
            " "
        )
            .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
            .setCancelable(false)
            .setCancelTextInfo(dialogCancelInfo(requireContext()))
            .setOkButton { dialog, v , input->
                if (mViewModel.checkString(input)) {
                    if(roomId == 0L){
                        // TODO: 添加设备到默认房间
                        mViewModel.addDeviceToRoom(deviceId, "", input, deviceType)
                        return@setOkButton false
                    }else{
                        when {
                            roomId != null -> {
                                mViewModel.addDeviceToRoom(deviceId, roomId.toString(), input, deviceType)
                            }
                            else -> {
                                "未知错误".toast()
                            }
                        }
                    }
                }
                true
            }
            .setCancelButton { _, _ ->
                false
            }
    }
}