package com.example.servicecreate.ui.home.adapter

import android.bluetooth.BluetoothDevice
import com.example.base.ui.activity.BaseAdapter
import com.example.servicecreate.databinding.ItemBlueDeviceBinding
import com.example.servicecreate.logic.db.model.MyDevice
import com.example.servicecreate.ui.home.gateway.GatewayFragment

/**
 * @author:SunShibo
 * @date:2023-04-08 23:25
 * @feature:
 */
class BlueRecyclerAdapter(private val fragment: GatewayFragment): BaseAdapter<MyDevice, ItemBlueDeviceBinding>(){
    override fun ItemBlueDeviceBinding.onBindViewHolder(bean: MyDevice, position: Int) {
        tvName.text = bean.device.name
        tvAddress.text = bean.device.address
        tvRssi.text = " ${bean.rssi} dBm"

        itemBlue.setOnClickListener {

            fragment.connectDevice(bean.device)
        }
    }
}