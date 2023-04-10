package com.example.servicecreate.ui.home.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base.kxt.toast
import com.example.base.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentHomeBinding
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.home.MainListener
import com.example.servicecreate.ui.home.adapter.DevicesRecyclerAdapter
import com.example.servicecreate.ui.home.adapter.HomeRecyclerAdapter
import com.example.servicecreate.ui.home.append.AppendFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(), MainListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var roomAdapter : HomeRecyclerAdapter
    private lateinit var deviceAdapter : HomeRecyclerAdapter
    private lateinit var devicesAdapter: DevicesRecyclerAdapter
    internal val mViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun FragmentHomeBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.mainListener = this@HomeFragment

        initData()

        roomAdapter = HomeRecyclerAdapter(this@HomeFragment)
        deviceAdapter = HomeRecyclerAdapter(this@HomeFragment)
        devicesAdapter = DevicesRecyclerAdapter(this@HomeFragment)
        homeRecyclerview.adapter = roomAdapter

        homeUsername.text = "Hello, ${ServiceCreateApplication.appSecret.split(".")}"
        Log.e("token", ServiceCreateApplication.appSecret.toString())

        homeNotification.apply {
            setOnClickListener {
                if(mViewModel.isRoom){
                    AppendFragment(0).show(parentFragmentManager, "Append")
                }else{
                    AppendFragment(1).show(parentFragmentManager, "Append")
                }
            }
        }

        homeTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == 0){
                    homeRecyclerview.adapter = roomAdapter
                    homeMode.visibility = View.GONE
                    mViewModel.isRoom = true
                }else{
                    homeRecyclerview.adapter = deviceAdapter
                    homeMode.visibility = View.VISIBLE
                    mViewModel.isRoom = false
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        homeSwipeRefresh.apply {
            setColorSchemeColors(
                resources.getColor(R.color.main_color),
                resources.getColor(R.color.main_color_primary_variant),
                resources.getColor(R.color.main_color_primary_secondary)
            )
            setOnRefreshListener(this@HomeFragment)
        }

        homeMode.setOnClickListener {
            mViewModel.modeShift()
        }

        with(mViewModel){
            lifecycleScope.launch {
                _refresh.collect{
                    //todo: delete this
                    homeUsername.text = it.toString()
                    initData()
                }
            }
            lifecycleScope.launch{
                _modeShift.collect{
                    if(it){
                        homeRecyclerview.adapter = devicesAdapter
                    }else{
                        homeRecyclerview.adapter = deviceAdapter
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * 通信用回调
         */
        setFragmentResultListener("refreshKey"){
                _, bundle ->
            val res = bundle.getString("bundleKey")
            if(res != null){
                mViewModel.refreshHomePage()
            }
        }
    }

    private fun initData() {
        mViewModel.getRoomList()
        mViewModel.getDeviceKind()
        mViewModel.getDeviceList()
    }

    override fun onRefresh() {
        mViewModel.refreshHomePage()
        val job = Job()
        CoroutineScope(job).launch {
            withContext(Dispatchers.Main) {
                delay(3000)
                binding.homeSwipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onGetRoomList(result: LiveData<Result<RoomListResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            roomAdapter.setData(response?.data)
        }
    }

    override fun onDeleteRoom(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(R.string.home_room_delete_success)
                    mViewModel.refreshHomePage()
                }else{
                    requireContext().toast(R.string.home_room_delete_failure)
                    Log.e("delete", response.msg + response.data)
                }
            }
        }
    }

    override fun onGetDeviceKindList(result: LiveData<Result<RoomListResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            deviceAdapter.setData(response?.data)
        }
    }

    override fun onGetDeviceList(result: LiveData<Result<DeviceListResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            devicesAdapter.setData(response?.data)
        }
    }

    override fun onDeleteDevice(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(R.string.home_device_delete_success)
                    mViewModel.refreshHomePage()
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

    override fun onControllerResult(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("房间or classify: ${response.data}")
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }
}