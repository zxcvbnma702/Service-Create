package com.example.servicecreate.ui.home.exhibit

import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.base.kxt.addOnBackPressed
import com.example.base.kxt.toast
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentExhibitBinding
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.ui.home.MainListener
import com.example.servicecreate.ui.home.adapter.ExhibitRecyclerAdapter
import com.example.servicecreate.ui.home.adapter.HomeRecyclerAdapter
import com.example.servicecreate.ui.home.home.HomeViewModel
import com.example.servicecreate.ui.toast

class ExhibitFragment(private val l: Long) : BaseFragment<FragmentExhibitBinding>() , ExhibitListener{

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
            else ->{
                exhibitToolbar.title = "默认房间"
                exhibitToolbar.subtitle = l.toString()
            }
        }

        exhibitToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        initData()

    }

    private fun initData() {
        mViewModel.getRoomDetail(l)
        mViewModel.getRoomDevices(l)
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
}
