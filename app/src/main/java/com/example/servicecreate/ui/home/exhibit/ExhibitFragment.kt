package com.example.servicecreate.ui.home.exhibit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.kxt.toast
import com.example.base.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentExhibitBinding
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.home.adapter.ExhibitRecyclerAdapter
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
                exhibitToolbar.title = roomName
                exhibitToolbar.subtitle = l.toString()
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

    private fun initData() {
        if(l < 100){
            mViewModel.getDeviceListByType(l.toInt())
        }else{
            mViewModel.getRoomDetail(l)
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
}
