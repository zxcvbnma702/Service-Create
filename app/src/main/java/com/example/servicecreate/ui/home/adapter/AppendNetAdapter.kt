package com.example.servicecreate.ui.home.adapter

import com.example.base.activity.BaseAdapter
import com.example.servicecreate.databinding.ItemAppendDeviceCardBinding
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.ui.home.append.AppendFragment

/**
 * @author:SunShibo
 * @date:2023-04-09 16:21
 * @feature:
 */
class AppendNetAdapter(private val fragment: AppendFragment,
) : BaseAdapter<DeviceData, ItemAppendDeviceCardBinding>() {
    override fun ItemAppendDeviceCardBinding.onBindViewHolder(bean: DeviceData, position: Int) {

    }

}