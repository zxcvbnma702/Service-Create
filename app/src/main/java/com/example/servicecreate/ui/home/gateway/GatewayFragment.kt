package com.example.servicecreate.ui.home.gateway

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.annotation.Nullable
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import cn.wandersnail.bluetooth.*
import cn.wandersnail.commons.observer.Observe
import cn.wandersnail.commons.poster.RunOn
import cn.wandersnail.commons.poster.Tag
import cn.wandersnail.commons.poster.ThreadMode
import com.example.base.kxt.toast
import com.example.base.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentGatewayBinding
import com.example.servicecreate.logic.db.model.MyDevice
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.home.adapter.BlueRecyclerAdapter
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.OnBindView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GatewayFragment: BaseFragment<FragmentGatewayBinding>(),GatewayListener, DiscoveryListener,
    EventObserver {

    private lateinit var connection: Connection
    private lateinit var macDialog: MessageDialog
    private lateinit var addWifi: MessageDialog
    private lateinit var adapter: BlueRecyclerAdapter
    private var bluelist = mutableListOf<MyDevice>()
    private var isScanning = false
    private var showMac = 0

    internal val mViewModel: GatewayViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[GatewayViewModel::class.java]
    }

    override fun FragmentGatewayBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.gatewayListener = this@GatewayFragment

        adapter = BlueRecyclerAdapter(this@GatewayFragment)
        gatewayRecycler.adapter = adapter

        BTManager.getInstance().addDiscoveryListener(this@GatewayFragment);
        BTManager.getInstance().registerObserver(this@GatewayFragment);

        if(ServiceCreateApplication.sp.getBoolean(ServiceCreateApplication.isGateAay, false)){
            gatewayAdded.visibility = View.VISIBLE
        }

        gatewayToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        gatewayAdd.setOnClickListener {
            if(isScanning){
                BTManager.getInstance().stopDiscovery()
                isScanning = false
                gatewayAdd.text = "点击搜索网关设备"
            }else{
                BTManager.getInstance().startDiscovery();
                isScanning = true
                gatewayAdd.text = "搜索中..."
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    internal fun connectDevice(address: BluetoothDevice){
        connection = BTManager.getInstance()
            .createConnection(address, UUIDWrapper.useDefault(), this)!! //观察者监听连接状态

        connection.connect(object : ConnectCallback {
            override fun onSuccess() {
                Log.e("connect", "设备连接成功")
                GlobalScope.launch(Dispatchers.Main) {
                    showWIFIDialog(address)
                }
            }
            override fun onFail(errMsg: String, @Nullable e: Throwable?) {
               Log.e("connect", errMsg)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun showWIFIDialog(device: BluetoothDevice) {
        addWifi = MessageDialog.show(
            "请向网关设备: ${device.name} 输入您的WIFI账户和密码",
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
                        mViewModel.WIFIName =  it.toString()
                    }
                    val deviceName = v?.findViewById<EditText>(R.id.custom_input_device_name)
                    deviceName?.doAfterTextChanged {
                        mViewModel.WIFIPassword =  it.toString()
                    }
                }

            })
            .setCancelTextInfo(dialogCancelInfo(requireContext()))
            .setOkButton { dialog, v ->
                if(mViewModel.hasBytes()){
                    val data = mViewModel.WIFIName +","+ mViewModel.WIFIPassword
                    val bytes = data.encodeToByteArray()
                    Log.e("data", bytes.toString() + bytes.decodeToString())
                    sendData(bytes)
                }
                true
            }
            .setCancelButton { _, _ ->
                false
            }
    }

    @SuppressLint("MissingPermission")
    @OptIn(DelicateCoroutinesApi::class)
    private fun sendData(bytes: ByteArray){
        connection.write("phone", bytes
        ) { device, tag, value, result ->
            if (result) {
                Log.e("writeData", "${device.name}: 数据写入成功")
                GlobalScope.launch(Dispatchers.Main) {
                    addWifi.dismiss()
                }
            } else {
                Log.e("writeData", "${device.name}: 数据写入失败")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BTManager.getInstance().removeDiscoveryListener(this);
        BTManager.getInstance().unregisterObserver(this);
    }

    override fun onDiscoveryStart() {
        "搜索开始".toast()
    }

    override fun onDiscoveryStop() {
        "搜索结束".toast()
        binding.gatewayAdd
            .text = "点击搜索网关设备"
    }

    @SuppressLint("MissingPermission")
    override fun onDeviceFound(device: BluetoothDevice, rssi: Int) {
        bluelist.add(MyDevice(device, rssi))
        adapter.setData(bluelist)
        adapter.notifyDataSetChanged()

        Log.e("device", device.name + device.address)
    }

    override fun onDiscoveryError(errorCode: Int, errorMsg: String) {

    }

    @SuppressLint("MissingPermission")
    @Observe
    override fun onRead(device: BluetoothDevice, wrapper: UUIDWrapper, value: ByteArray) {
        super.onRead(device, wrapper, value)
        Log.e("receiveData", device?.name + value.decodeToString())
        BTManager.getInstance().disconnectConnection(device, UUIDWrapper.useDefault())
        addWifi.dismiss()
        binding.gatewayAdded.visibility = View.VISIBLE
        ServiceCreateApplication.sp.edit().putBoolean(ServiceCreateApplication.isGateAay, true).apply()
    }

    @SuppressLint("MissingPermission")
    override fun onWrite(
        device: BluetoothDevice,
        wrapper: UUIDWrapper,
        tag: String,
        value: ByteArray,
        result: Boolean
    ) {
        super.onWrite(device, wrapper, tag, value, result)
        if(result){
            Log.e("writeData", "${device.name}ok")
        }else{
            Log.e("writeData", "${device.name}no")
        }
    }

    @Tag("onConnectionStateChanged")
    @Observe
    @RunOn(ThreadMode.MAIN)
    override fun onConnectionStateChanged(
        device: BluetoothDevice,
        wrapper: UUIDWrapper,
        state: Int
    ) {
        super.onConnectionStateChanged(device, wrapper, state)
        when (state) {
            Connection.STATE_CONNECTING -> { "正在连接中".toast()}
            Connection.STATE_PAIRING -> {Log.e("connectState", "配对中")}
            Connection.STATE_PAIRED -> {Log.e("connectState", "已配对")}
            Connection.STATE_CONNECTED -> {"已连接".toast()}
            Connection.STATE_DISCONNECTED -> {Log.e("connectState", "取消连接")}
            Connection.STATE_RELEASED -> {Log.e("connectState","释放连接" )}
        }
    }
}