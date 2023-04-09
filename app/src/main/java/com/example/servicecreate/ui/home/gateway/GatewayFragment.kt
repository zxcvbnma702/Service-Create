package com.example.servicecreate.ui.home.gateway

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
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
import com.sdwfqin.cbt.CbtManager
import com.sdwfqin.cbt.callback.ConnectDeviceCallback
import com.sdwfqin.cbt.callback.SendDataCallback


class GatewayFragment: BaseFragment<FragmentGatewayBinding>(),GatewayListener {

    private lateinit var macDialog: MessageDialog
    private lateinit var addWifi: MessageDialog
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var scanner: BluetoothLeScanner
    private var isScanning = false
    private lateinit var adapter: BlueRecyclerAdapter

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

        if(isOpenBluetooth()){
            val manager = requireContext().getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothAdapter = manager.adapter
            scanner = bluetoothAdapter.bluetoothLeScanner
        }

        gatewayAdd.setOnClickListener {
            //开启蓝牙
            if(isOpenBluetooth()){
               "蓝牙已开启".toast()
                val manager = requireContext().getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
                bluetoothAdapter = manager.adapter
                scanner = bluetoothAdapter.bluetoothLeScanner
            }else{
                if (isAndroid12()) {
                    //检查是否有BLUETOOTH_CONNECT权限
                    if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                        //打开蓝牙
                        enableBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                    } else {
                        //请求权限
                        requestBluetoothConnect.launch(Manifest.permission.BLUETOOTH_CONNECT)
                    }
                    return@setOnClickListener
                }
                enableBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }

            //扫描蓝牙
            if (isAndroid12()) {
                if (hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
                    //扫描或者停止扫描
                    if (isScanning) stopScan() else startScan()
                } else {
                    //请求权限
                    requestBluetoothScan.launch(Manifest.permission.BLUETOOTH_SCAN)
                }
            }else{
                if(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
                    //扫描或者停止扫描
                    if (isScanning) stopScan() else startScan()
                }else{
                    requestBluetoothScan.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    internal fun connectDevice(address: BluetoothDevice){
        CbtManager.getInstance().connectDevice(address, object: ConnectDeviceCallback {
            override fun connectSuccess(socket: BluetoothSocket?, device: BluetoothDevice?) {
                (device?.name + "链接成功").toast()
                if (device != null) {
                    showWIFIDialog(device)
                }
            }

            override fun connectError(throwable: Throwable?) {
                "链接错误".toast()
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
                Log.e("byte",mViewModel.bytes.decodeToString())
                if(mViewModel.hasBytes()){
                    sendData(mViewModel.bytes)
                }
                true
            }
            .setCancelButton { _, _ ->
                false
            }
    }

    private fun sendData(bytes: ByteArray){
        CbtManager
            .getInstance()
            .sendData(bytes, object : SendDataCallback {
                override fun sendSuccess() {
                    "数据发送成功".toast()
                    addWifi.dismiss()
                    showMACDialog()
                }

                override fun sendError(throwable: Throwable) {
                    // 发送失败
                    Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
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

    //蓝牙是否打开
    private fun isOpenBluetooth(): Boolean {
        val manager = requireContext().getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = manager.adapter ?: return false
        return adapter.isEnabled
    }

    private fun isAndroid12() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    private fun hasPermission(permission: String) =
        checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

    //打开蓝牙意图
    private val enableBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            requireContext().toast(if (isOpenBluetooth()) "蓝牙已打开" else "蓝牙未打开")
        }
    }

    //请求BLUETOOTH_CONNECT权限意图
    private val requestBluetoothConnect = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            //打开蓝牙
            enableBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        } else {
            requireContext().toast("Android12中未获取此权限，则无法打开蓝牙。")
        }
    }

    //扫描结果回调
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addDeviceList(MyDevice(result.device, result.rssi))
        }
    }

    //请求BLUETOOTH_SCAN权限意图
    private val requestBluetoothScan =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                //进行扫描
                startScan()
            } else {
                "Android12中未获取此权限，则无法扫描蓝牙。".toast()
            }
        }

    //请求定位权限意图
    private val requestLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                //扫描蓝牙
                startScan()
            } else {
                "Android12以下，6及以上需要定位权限才能扫描设备".toast()
            }
        }

    private fun startScan() {
        if (!isScanning) {
            scanner.startScan(scanCallback)
            isScanning = true
            binding.gatewayAdd.text = "停止扫描"
        }
    }

    private fun stopScan() {
        if (isScanning) {
            scanner.stopScan(scanCallback)
            isScanning = false
            binding.gatewayAdd.text = "继续扫描"
        }
    }

    private fun findDeviceIndex(scanDevice: MyDevice, deviceList: List<MyDevice>): Int {
        var index = 0
        for (device in deviceList) {
            if (scanDevice.device.address.equals(device.device.address)) return index
            index += 1
        }
        return -1
    }

    private fun addDeviceList(device: MyDevice) {
        val index = findDeviceIndex(device, mViewModel.deviceList)
        if (index == -1) {
            Log.d(TAG, "name: ${device.device.name}, address: ${device.device.address}")
            mViewModel.deviceList.add(device)
            adapter.setData(mViewModel.deviceList)
        } else {
            mViewModel.deviceList[index].rssi = device.rssi
            adapter.notifyItemChanged(index)
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
}