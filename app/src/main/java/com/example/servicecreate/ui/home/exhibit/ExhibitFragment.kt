package com.example.servicecreate.ui.home.exhibit

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.activity.BaseFragment
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentExhibitBinding
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.dialogTitleInfo
import com.example.servicecreate.ui.home.adapter.ExhibitRecyclerAdapter
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener
import kotlinx.coroutines.launch


class ExhibitFragment(internal val l: Long, private val roomName: String) : BaseFragment<FragmentExhibitBinding>() , ExhibitListener{

    private lateinit var roomsDeviceAdapter : ExhibitRecyclerAdapter
    private lateinit var kindDeviceAdapter : ExhibitRecyclerAdapter
    internal val mViewModel: ExhibitViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ExhibitViewModel::class.java]
    }

    override fun FragmentExhibitBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.exhibitListener = this@ExhibitFragment

        roomsDeviceAdapter = ExhibitRecyclerAdapter(this@ExhibitFragment)
        kindDeviceAdapter = ExhibitRecyclerAdapter(this@ExhibitFragment)
        exhibitRecyclerview.adapter = roomsDeviceAdapter

        when(l){
            1L -> exhibitToolbar.title = "所有的空调"
            2L -> exhibitToolbar.title = "所有的灯"
            3L -> exhibitToolbar.title = "所有的门锁"
            4L -> exhibitToolbar.title = "所有的彩灯"
            5L -> exhibitToolbar.title = "所有的摄像头"
            else ->{
                exhibitToolbar.apply{
                    title = roomName
//                    subtitle = l.toString()
                    inflateMenu(R.menu.exhibit_menu);
                    setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.exhibit_more -> {
                                roomController()
                                false
                            }
                            else -> {
                                false
                            }
                        }
                    }
                }
            }
        }

        exhibitToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        initData()

        with(mViewModel){
            lifecycleScope.launch {
                _refresh.collect{
                    initData()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        if(l < 100){
            mViewModel.getDeviceListByType(l.toInt())
        }else{
//            mViewModel.getRoomDetail(l)
            mViewModel.getRoomDevices(l)
        }
        Log.e("id", l.toString())
    }

    override fun onRoomDetail(result: LiveData<Result<RoomListResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if(response != null){
                if(response.code == 1){
                    binding.exhibitToolbar.title = response.data.first().name
                    binding.exhibitToolbar.subtitle = response.data.first().updateTime
                }else{
                    requireContext().toast(R.string.exhibit_room_detail_failure)
                }
            }else{
                requireContext().toast(R.string.exhibit_room_detail_failure)
            }
        }
    }

    override fun onRoomDevice(result: LiveData<Result<DeviceListResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if(response != null){
                if(response.code == 1){
                    roomsDeviceAdapter.setData(response.data)
//                    Log.e("ff", response.data.toString())
                }else{
                    requireContext().toast(R.string.exhibit_room_device_failure)
                }
            }else{
                requireContext().toast(R.string.exhibit_room_device_failure)
            }
        }
    }

    override fun onDeviceListByType(result: LiveData<Result<DeviceListResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if(response != null){
                if(response.code == 1){
                    roomsDeviceAdapter.setData(response.data)
                }else{
                    requireContext().toast(R.string.exhibit_kind_device_failure)
                }
            }else{
                requireContext().toast(R.string.exhibit_kind_device_failure)
            }
        }
    }

    override fun onDeleteDevice(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(R.string.home_device_delete_success)
                    mViewModel.refreshExhibitPage()
                }else{
                    requireContext().toast(R.string.home_device_delete_failure)
                }
            }
        }
    }

    override fun onSendLampState(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("灯: ${response.data}")
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onSendAirState(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("空调: ${response.data}")
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onSendLedState(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("led: ${response.data}")
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onSendDoorLockData(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("门锁: ${response.data}")
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onControllerByKind(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(response.data)
                    mViewModel.refreshExhibitPage()
                    roomsDeviceAdapter.notifyDataSetChanged()
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onGetRoomList(result: LiveData<Result<RoomListResponse>>) {
        result.observe(this){ re ->
            val responses = re.getOrNull()
            if (responses?.code == 1) {
                mViewModel.roomList.putAll(responses.data.associate {
                    it.name to it.id
                })
                showChangeDeviceRoomDialog()
            }else{
                "数据异常".toast()
            }
        }
    }

    override fun onChangeDeviceRoom(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    private fun roomController(){
        PopMenu.
        show(arrayOf("打开所有灯", "关闭所有灯", "打开所有空调", "关闭所有空调"))
            .setRadius(15f)
            .setMenuTextInfo(dialogTitleInfo(requireContext()))
            .setOnMenuItemClickListener { dialog, text, index ->
                when (index) {
                    0 -> mViewModel.controllerAllDeviceByKind(l, 2, 1)
                    1 -> mViewModel.controllerAllDeviceByKind(l, 2, 0)
                    2 -> mViewModel.controllerAllDeviceByKind(l, 1, 1)
                    3 -> mViewModel.controllerAllDeviceByKind(l, 1, 0)
                }
                false
            }
            .onIconChangeCallBack =
            object : OnIconChangeCallBack<PopMenu?>(true) {
                override fun getIcon(dialog: PopMenu?, index: Int, menuText: String): Int {
                    return when (index) {
                        0 -> R.drawable.lamp_state
                        1 -> R.drawable.lamp_state
                        2 -> R.drawable.air_state
                        3 -> R.drawable.air_state
                        else -> 0
                    }
                }
            }
    }

    internal fun roomDeviceSelect(id: Long) {
        mViewModel.deviceId = id.toInt()
        PopMenu.
        show(arrayOf("移动到其他房间", "删除设备"))
            .setRadius(15f)
            .setMenuTextInfo(dialogTitleInfo(requireContext()))
            .setOnMenuItemClickListener { dialog, text, index ->
                when (index) {
                    0 -> mViewModel.getRoomList()
                    1 -> mViewModel.deleteDevice(mViewModel.deviceId.toString())
                }
                false
            }
            .onIconChangeCallBack =
            object : OnIconChangeCallBack<PopMenu?>(true) {
                override fun getIcon(dialog: PopMenu?, index: Int, menuText: String): Int {
                    return when (index) {
                        0 -> R.drawable.ic_baseline_swap_horiz_24
                        1 -> R.drawable.ic_outline_delete_24
                        else -> 0
                    }
                }
            }
    }

    private fun showChangeDeviceRoomDialog(){
        PopMenu.show(mViewModel.roomList.keys.toList())
            .setRadius(15f)
            .setOnMenuItemClickListener { dialog, text, index ->
                mViewModel.changeDeviceRoom(mViewModel.deviceId.toString(), mViewModel.roomList[text].toString())
                false
            }.menuTextInfo = dialogTitleInfo(requireContext())
    }

}
