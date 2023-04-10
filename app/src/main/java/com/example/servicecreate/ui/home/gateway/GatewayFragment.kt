package com.example.servicecreate.ui.home.gateway

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import cn.wandersnail.bluetooth.*
import cn.wandersnail.bluetooth.DiscoveryListener.*
import cn.wandersnail.commons.observer.Observe
import cn.wandersnail.commons.poster.RunOn
import cn.wandersnail.commons.poster.Tag
import cn.wandersnail.commons.poster.ThreadMode
import com.example.base.kxt.toast
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.R
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
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var scanner: BluetoothLeScanner
    private lateinit var adapter: BlueRecyclerAdapter
    private var bluelist = mutableListOf<MyDevice>()
    private var isScanning = false

    private val TAG = "blue"

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
                    Log.e("data", mViewModel.WIFIName + " " + mViewModel.WIFIPassword)
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

    private fun sendData(bytes: ByteArray){
        connection.write("phone", bytes
        ) { device, tag, value, result ->
            if (result) {
                Log.e("writeData", "${device.name}: 数据写入成功")
            } else {
                Log.e("writeData", "${device.name}: 数据写入失败")
            }
        }
    }

    private fun showMACDialog() {
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

    override fun onSendMac(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast(response.msg + response.data)
                    macDialog.dismiss()
                    binding.gatewayAdded.visibility = View.VISIBLE
                }else{
                    requireContext().toast(response.msg)
                }
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
    }

    override fun onDeviceFound(device: BluetoothDevice, rssi: Int) {
        bluelist.add(MyDevice(device, rssi))
        adapter.setData(bluelist)
        adapter.notifyDataSetChanged()

        Log.e("device", device.name + device.address)
    }

    override fun onDiscoveryError(errorCode: Int, errorMsg: String) {

    }

    @Observe
    override fun onRead(device: BluetoothDevice, wrapper: UUIDWrapper, value: ByteArray) {
        super.onRead(device, wrapper, value)
        Log.e("receiveData", device?.name + value.decodeToString())
        BTManager.getInstance().disconnectConnection(device, UUIDWrapper.useDefault())
        addWifi.dismiss()
        showMACDialog()
    }

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