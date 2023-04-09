package com.example.servicecreate.ui.home.adapter

import com.bumptech.glide.Glide
import com.example.base.ui.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemHomeRoomCardBinding
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.ui.controller.ControllerActivity
import com.example.servicecreate.ui.home.home.HomeFragment
import com.kongzue.dialogx.dialogs.MessageDialog
import kotlin.math.absoluteValue

/**
 * @author:SunShibo
 * @date:2023-04-01 18:00
 * @feature: 设备列表adapter
 */
class DevicesRecyclerAdapter(private val fragment: HomeFragment):
    BaseAdapter<DeviceData, ItemHomeRoomCardBinding>() {

    private val context =  fragment.requireContext()

    override fun ItemHomeRoomCardBinding.onBindViewHolder(bean: DeviceData, position: Int) {
        itemCardRoomName.text = bean.name
        itemCardRoomDescription.text = "上次使用: ${bean.updateTime}"
        when(bean.type){
            1 -> Glide.with(context).load(R.drawable.ic_device_air).into(itemCardRoomImage)
            2 -> Glide.with(context).load(R.drawable.ic_device_lamp).into(itemCardRoomImage)
            3 -> Glide.with(context).load(R.drawable.ic_device_door_lock).into(itemCardRoomImage)
            4 -> Glide.with(context).load(R.drawable.ic_device_led).into(itemCardRoomImage)
            5 -> Glide.with(context).load(R.drawable.ic_device_monitor).into(itemCardRoomImage)
        }

        if(bean.roomList.isNotEmpty()){
            itemCardRoomNumber.text = bean.roomList.first().name
        }else{
            itemCardRoomNumber.text = "默认房间"
        }

        itemCardHome.setOnClickListener {
            if(bean.roomList.isNotEmpty()){
                ControllerActivity.startActivity(context, bean.type, bean.id, bean.name, bean.roomList.first().name)
            }else{
                ControllerActivity.startActivity(context, bean.type, bean.id, bean.name, "默认房间")
            }
        }

        itemCardHome.setOnLongClickListener {
            MessageDialog.show(context.getString(R.string.device_delete_title), context.getString(R.string.home_delete_device_content), "确定", "取消")
                .setMaskColor(context.getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setOkButton { _, _ ->
                    fragment.mViewModel.deleteDevice(bean.id.toString())
                    false
                }
                .setCancelButton{_, _ ->
                    false
                }
            false
        }

        itemCardRoomSwitch.setOnCheckedChangeListener { _, b ->
            if(b){
                when(bean.type){
                    1 -> fragment.mViewModel.airState(bean.id.toInt().absoluteValue, 1)
                    2 -> fragment.mViewModel.lampState(bean.id.toInt().absoluteValue, 1)
                }
            }else{
                when(bean.type){
                    1 -> fragment.mViewModel.airState(bean.id.toInt().absoluteValue, 0)
                    2 -> fragment.mViewModel.lampState(bean.id.toInt().absoluteValue, 0)
                }
            }
        }


    }

}
