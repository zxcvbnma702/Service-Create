package com.example.servicecreate.ui.home.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base.activity.BaseFragment
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentHomeBinding
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.logic.network.model.WeatherResponse
import com.example.servicecreate.ui.home.MainListener
import com.example.servicecreate.ui.home.adapter.DevicesRecyclerAdapter
import com.example.servicecreate.ui.home.adapter.HomeRecyclerAdapter
import com.example.servicecreate.ui.home.append.AppendFragment
import com.example.servicecreate.ui.toast
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import java.lang.String.format
import java.text.MessageFormat.format

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

//        homeUsername.text = "Hello, ${ServiceCreateApplication.appSecret.split(".")}"
        Log.e("token", ServiceCreateApplication.appSecret.toString())

        homeCardViewDate.text = DateFormat.format("yyyy-MM-dd EEEE", System.currentTimeMillis())

        homeTips.text = "点击添加新房间->"

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
                    homeTips.text = "点击添加新房间->"
                }else{
                    homeRecyclerview.adapter = deviceAdapter
                    homeMode.visibility = View.VISIBLE
                    mViewModel.isRoom = false
                    homeTips.text = "点击添加新设备->"
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
        mViewModel.getWeather()
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

    override fun onWeather(result: LiveData<Result<WeatherResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.success == "1"){
//                    Log.e("r", response.result.toString())
                    binding.homeCardViewTem.text = response.result.realTime.wtNm + "   " + response.result.realTime.wtTemp + "℃"
                    binding.homeCardViewDate.text = response.result.realTime.week
                    binding.homeCardViewQuality.text = response.result.realTime.wtAqi + " AQI" + "\n PM2.5"
                    binding.homeCardViewHumidity.text = response.result.realTime.wtHumi + " %" + "\n 空气湿度"
                    binding.homeCardViewVisibility.text = response.result.realTime.wtVisibility + "\n 能见度"

                    val tem = response.result.realTime.wtTemp.toInt()
                    binding.homeCardViewQuality2.text = (tem - 4 .. tem + 2).random().toString() + " ℃" + "\n 房屋温度"
                    binding.homeCardViewHumidity2.text = (60..68).random().toString() + " %" + "\n 房屋湿度"
                    binding.homeCardViewVisibility2.text = (200..400).random().toString()  + " lx" + "\n 房屋光照"
                }else{
                    binding.homeCardViewTem.text = "天气数据初始化失败"
                }
            }
        }
    }
}